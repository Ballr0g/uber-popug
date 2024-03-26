package org.uber.popug.task.tracker.kafka.producer.event.business;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;

public interface TaskCompletedEventFactory {

    ProducerRecord<String, Object> createTaskCompletedEventV1(
            @Nonnull TaskEntity task,
            @Nonnull UserEntity assignee,
            @Nonnull String topicName
    );

}
