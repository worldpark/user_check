package com.check.user_check.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaTopicConfigUnitTest {

    @Test
    void attendanceOutboxTopicUsesExpectedNamePartitionsAndReplicas() {
        KafkaTopicConfig config = new KafkaTopicConfig();

        NewTopic topic = config.attendanceOutboxTopic();

        assertThat(topic.name()).isEqualTo("outbox.event.attendance");
        assertThat(topic.numPartitions()).isEqualTo(2);
        assertThat(topic.replicationFactor()).isEqualTo((short) 2);
    }
}
