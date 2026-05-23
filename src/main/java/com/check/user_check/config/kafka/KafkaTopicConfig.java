package com.check.user_check.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String ATTENDANCE_OUTBOX_TOPIC = "outbox.event.attendance";
    public static final String ATTENDANCE_OUTBOX_RETRY_TOPIC = "outbox.event.attendance.retry";
    public static final String ATTENDANCE_OUTBOX_DLT_TOPIC = "outbox.event.attendance.dlt";

    @Bean
    public NewTopic attendanceOutboxTopic(){
        return TopicBuilder.name(ATTENDANCE_OUTBOX_TOPIC)
                .partitions(2)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic attendanceOutboxRetryTopic(){
        return TopicBuilder.name(ATTENDANCE_OUTBOX_RETRY_TOPIC)
                .partitions(2)
                .replicas(2)
                .config("retention.ms", Long.toString(1000L * 60 * 30))
                .build();
    }

    @Bean
    public NewTopic attendanceOutboxDeadLetterTopic(){
        return TopicBuilder.name(ATTENDANCE_OUTBOX_DLT_TOPIC)
                .partitions(2)
                .replicas(2)
                .config("retention.ms", Long.toString(1000L * 60 * 60 * 24 * 7))
                .build();
    }
}
