package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.TaskForCreation;

public interface TaskAssignmentService {
    Task assignNewTask(TaskForCreation task);
}
