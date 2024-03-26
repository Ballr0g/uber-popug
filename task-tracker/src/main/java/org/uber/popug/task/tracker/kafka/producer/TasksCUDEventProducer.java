package org.uber.popug.task.tracker.kafka.producer;

import org.uber.popug.task.tracker.domain.task.Task;

public interface TasksCUDEventProducer {

    void sendTaskCreatedReplicationEvent(Task task);

}
