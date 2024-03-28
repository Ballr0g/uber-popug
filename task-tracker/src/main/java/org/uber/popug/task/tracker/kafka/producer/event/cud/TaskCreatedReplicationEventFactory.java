package org.uber.popug.task.tracker.kafka.producer.event.cud;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.uber.popug.task.tracker.domain.task.Task;

public interface TaskCreatedReplicationEventFactory {

    // Todo: more generic approach? <K, V, T> ProducerRecord<K, V> createEvent(T eventPayload, String name, int version)
    ProducerRecord<String, Object> createReplicationEvent(@Nonnull Task task, @Nonnull String topicName);

}
