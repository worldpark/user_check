package com.check.user_check.service.rag;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EsIndexService {

    private final EmbeddingModel embeddingModel;
    private final ElasticsearchClient elasticsearchClient;

    public void acceptDocuments(List<Document> documents){
        if(documents == null || documents.isEmpty()){
            return;
        }

        List<String> contents = documents.stream()
                .map(Document::getText)
                .toList();

        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(contents);

        for (int i = 0; i < documents.size(); i++) {
            Document document = documents.get(i);
            float[] vector = embeddingResponse
                    .getResults()
                    .get(i)
                    .getOutput();

            Map<String, Object> md = document.getMetadata() == null ?
                                                           Map.of() :
                                                           document.getMetadata();

            String source = String.valueOf(md.getOrDefault("source", "unknown"));
            String page = String.valueOf(md.getOrDefault("pageNumber", md.getOrDefault("page", "0")));
            String chunkId = source + "#p" + page + "-" + UUID.randomUUID();

            Map<String, Object> esDoc = new HashMap<>();
            esDoc.put("chunk_id", chunkId);
            esDoc.put("content", document.getText());
            esDoc.put("source", source);
            esDoc.put("page", page);
            esDoc.put("embedding", vector);

            IndexRequest<Map<String, Object>> request = IndexRequest.of(
                    mapBuilder -> mapBuilder.index("rag_chunks")
                            .id(chunkId)
                            .document(esDoc)
            );

            try {
                elasticsearchClient.index(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
