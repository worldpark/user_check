package com.check.user_check.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaTopicConfigUnitTest {

    @Test
    void attendanceOutboxTopicUsesExpectedNamePartitionsAndReplicas() {
        KafkaTopicConfig config = new KafkaTopicConfig();

        NewTopic topic = config.attendanceOutboxTopic();

        assertThat(topic.name()).isEqualTo(KafkaTopicConfig.ATTENDANCE_OUTBOX_TOPIC);
        assertThat(topic.numPartitions()).isEqualTo(2);
        assertThat(topic.replicationFactor()).isEqualTo((short) 2);
    }

    @Test
    void attendanceOutboxRetryTopicUsesExpectedNameAndRetention() {
        KafkaTopicConfig config = new KafkaTopicConfig();

        NewTopic topic = config.attendanceOutboxRetryTopic();

        assertThat(topic.name()).isEqualTo(KafkaTopicConfig.ATTENDANCE_OUTBOX_RETRY_TOPIC);
        assertThat(topic.numPartitions()).isEqualTo(2);
        assertThat(topic.replicationFactor()).isEqualTo((short) 2);
        assertThat(topic.configs()).containsEntry("retention.ms", Long.toString(1000L * 60 * 30));
    }

    @Test
    void attendanceOutboxDeadLetterTopicUsesExpectedNameAndRetention() {
        KafkaTopicConfig config = new KafkaTopicConfig();

        NewTopic topic = config.attendanceOutboxDeadLetterTopic();

        assertThat(topic.name()).isEqualTo(KafkaTopicConfig.ATTENDANCE_OUTBOX_DLT_TOPIC);
        assertThat(topic.numPartitions()).isEqualTo(2);
        assertThat(topic.replicationFactor()).isEqualTo((short) 2);
        assertThat(topic.configs()).containsEntry("retention.ms", Long.toString(1000L * 60 * 60 * 24 * 7));
    }
}
