package org.uber.popug.task.tracker.kafka.producer.event.cud;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.uber.popug.task.tracker.domain.task.Task;

public interface TaskCreatedReplicationEventFactory {

    ProducerRecord<String, Object> createdReplicationEventV1(@Nonnull Task task, @Nonnull String topicName);

}
