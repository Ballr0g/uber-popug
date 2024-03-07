package org.uber.popug.task.tracker.kafka.producer;

import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.dto.TaskReassignedEvent;

import java.util.List;

public interface TasksCUDEventProducer {

    void sendTaskCreationEvent(Task task);

    void sendTaskReassignmentEvents(List<TaskReassignedEvent> taskReassignedEvents);

}
