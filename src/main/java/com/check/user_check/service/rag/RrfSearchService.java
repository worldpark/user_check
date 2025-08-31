package com.check.user_check.service.rag;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RrfSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Data
    @AllArgsConstructor
    public static class RrfHit{
        private String id;
        private double rrfScore;
        private Double originalScore;
        private Map<String, Object> source;
    }

    @FunctionalInterface
    public interface SearchRunner{
        List<RrfHit> run() throws IOException;
    }

    public List<RrfHit> rrfFuse(List<SearchRunner> runners) throws IOException{
        Rrf rrf = new Rrf(60);
        Map<String, RrfHit> byId = new HashMap<>();

        for (SearchRunner runner : runners) {
            List<RrfHit> hits = runner.run();

            int rank = 0;

            for (RrfHit hit : hits) {
                rank++;

                double add = rrf.contribution(rank);

                RrfHit agg = byId.get(hit.getId());


                if(agg == null){
                    agg = new RrfHit(hit.getId(), add, hit.getOriginalScore(), hit.getSource());
                    byId.put(hit.getId(), agg);
                }else{
                    agg.setRrfScore(agg.getRrfScore() + add);
                }
            }
        }

        return byId.values().stream()
                .sorted(Comparator.comparingDouble(RrfHit::getRrfScore).reversed())
                .toList();
    }

    public SearchRunner bm25Runner(String index, String field, String query, int size){
        return () -> {
            SearchResponse<Map> response = elasticsearchClient.search(req -> req
                    .index(index)
                    .size(size)
                    .query(q -> q
                            .match(m -> m.field(field)
                                    .query(query)
                            )
                    ), Map.class
            );

            List<Hit<Map>> hits = response.hits().hits();
            List<RrfHit> output = new ArrayList<>();

            for (Hit<Map> hit : hits) {
                output.add(new RrfHit(
                        hit.id(), 0.0, hit.score(), hit.source()
                ));
            }

            return output;
        };
    }

    public SearchRunner queryStringRunner(String index, String queryString, int size){
        return () -> {
            SearchResponse<Map> response = elasticsearchClient.search(req -> req
                    .index(index)
                    .size(size)
                    .query(q -> q
                            .queryString(qs -> qs
                                    .query(queryString))
                    ), Map.class
            );

            List<RrfHit> output = new ArrayList<>();
            for (Hit<Map> hit : response.hits().hits()) {
                output.add(new RrfHit(hit.id(), 0.0, hit.score(), hit.source()));
            }

            return output;
        };
    }

    public SearchRunner knnRunner(
            String index, String vectorField, List<Float> queryVector, int k, int numCandidates){

        return () -> {
            SearchResponse<Map> response = elasticsearchClient.search(req -> req
                    .index(index)
                    .knn(knn -> knn
                            .field(vectorField)
                            .queryVector(queryVector)
                            .k(k)
                            .numCandidates(numCandidates))
                    .size(k), Map.class
            );

            List<RrfHit> output = new ArrayList<>();

            for (Hit<Map> hit : response.hits().hits()) {
                output.add(new RrfHit(hit.id(), 0.0, hit.score(), hit.source()));
            }

            return output;
        };
    }
}
