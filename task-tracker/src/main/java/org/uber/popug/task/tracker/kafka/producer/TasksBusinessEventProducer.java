package org.uber.popug.task.tracker.kafka.producer;

import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCompletedEvent;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskReassignedEvent;

import java.util.List;

public interface TasksBusinessEventProducer {

    void sendTaskCreationEvent(Task task);

    void sendTaskReassignmentEvents(List<TaskReassignedEvent> taskReassignedEvents);

    void sendTaskCompletionEvent(TaskCompletedEvent taskCompletedEvent);

}
