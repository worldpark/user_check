package com.check.user_check.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic attendanceOutboxTopic(){
        return TopicBuilder.name("outbox.event.attendance")
                .partitions(2)
                .replicas(2)
                .build();
    }
}
