package org.uber.popug.task.tracker.kafka.producer.event.business;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.uber.popug.task.tracker.entity.composite.ReassignedTaskEntity;

public interface TaskReassignedEventFactory {

    ProducerRecord<String, Object> createTaskReassignedEvent(
            @Nonnull ReassignedTaskEntity reassignedTask,
            @Nonnull String topicName
    );

}
