package com.check.user_check.service.rag;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RrfSearchServiceUnitTest {

    @Test
    void rrfFuseAccumulatesDuplicateIdsAndSortsByCombinedScore() throws IOException {
        RrfSearchService service = new RrfSearchService(mock(ElasticsearchClient.class));

        List<RrfSearchService.RrfHit> result = service.rrfFuse(List.of(
                () -> List.of(
                        new RrfSearchService.RrfHit("doc-1", 0.0, 10.0, Map.of("title", "first")),
                        new RrfSearchService.RrfHit("doc-2", 0.0, 9.0, Map.of("title", "second"))
                ),
                () -> List.of(
                        new RrfSearchService.RrfHit("doc-2", 0.0, 8.0, Map.of("title", "second")),
                        new RrfSearchService.RrfHit("doc-3", 0.0, 7.0, Map.of("title", "third"))
                )
        ));

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo("doc-2");
        assertThat(result.get(1).getId()).isEqualTo("doc-1");
        assertThat(result.get(2).getId()).isEqualTo("doc-3");
        assertThat(result.get(0).getRrfScore()).isEqualTo((1.0 / 62.0) + (1.0 / 61.0));
    }
}
