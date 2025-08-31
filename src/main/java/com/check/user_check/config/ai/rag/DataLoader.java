package com.check.user_check.config.ai.rag;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.check.user_check.service.rag.EsIndexService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoader {
    private final EsIndexService esIndexService;
    private final ElasticsearchClient elasticsearchClient;

    @Value("classpath:/출석_체크_시스템_가이드.pdf")
    private Resource resource;

    @PostConstruct
    public void init(){

        long dataCount = 0;
        try {
            dataCount = elasticsearchClient.count(client -> client.index("rag_chunks")).count();

        }catch (ElasticsearchException exception){
            if(exception.status() == 404){
                log.warn("해당 Index가 존재하지 않음");
            }
            
            throw exception;
        }catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        if(dataCount == 0){

            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageTopMargin(0)
                    .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                            .withNumberOfTopTextLinesToDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();

            PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(resource, config);
            List<Document> documents = pdfDocumentReader.get();

            TokenTextSplitter splitter =
                    new TokenTextSplitter(600, 200, 10, 5000, true);

            List<Document> spliterDocuments = splitter.apply(documents);

            esIndexService.acceptDocuments(spliterDocuments);
        }
    }
}
