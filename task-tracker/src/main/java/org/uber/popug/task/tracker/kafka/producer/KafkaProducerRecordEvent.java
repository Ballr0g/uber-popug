package org.uber.popug.task.tracker.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaProducerRecordEvent<K, V> {

    ProducerRecord<K, V> asProducerRecord(String topicName);

    K recordKey();

    V recordValue();

}
