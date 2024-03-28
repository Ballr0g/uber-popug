package org.uber.popug.task.tracker.kafka.producer.event.business;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.uber.popug.task.tracker.domain.task.Task;

public interface TaskCreatedEventFactory {

    ProducerRecord<String, Object> createTaskCreatedEvent(@Nonnull Task task, @Nonnull String topicName);

}
