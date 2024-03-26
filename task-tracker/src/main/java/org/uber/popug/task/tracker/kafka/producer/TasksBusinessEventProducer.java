package org.uber.popug.task.tracker.kafka.producer;

import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.entity.composite.ReassignedTaskEntity;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;

import java.util.List;

public interface TasksBusinessEventProducer {

    void sendTaskCreationEvent(Task task);

    void sendTaskReassignmentEventsTransactional(List<ReassignedTaskEntity> tasksForReassignment);

    void sendTaskCompletionEvent(TaskEntity task, UserEntity assignee);

}
