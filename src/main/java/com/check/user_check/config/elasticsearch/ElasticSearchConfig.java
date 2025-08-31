package com.check.user_check.config.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.server-url}")
    private String elasticsearchServerURL;

    @Value("${spring.elasticsearch.username}")
    private String elasticsearchUsername;

    @Value("${spring.elasticsearch.password}")
    private String elasticsearchPassword;

    @Bean(destroyMethod = "close")
    public ElasticsearchClient elasticsearchClient(){

        return ElasticsearchClient.of(builder -> builder
                .host(elasticsearchServerURL)
                .usernameAndPassword(elasticsearchUsername, elasticsearchPassword)
        );
    }
}
