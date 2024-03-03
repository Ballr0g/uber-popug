package org.uber.popug.task.tracker.service.impl;

import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.TaskForCreation;
import org.uber.popug.task.tracker.exception.NotImplementedException;
import org.uber.popug.task.tracker.service.TaskAssignmentService;

public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    @Override
    public Task assignNewTask(TaskForCreation task) {
        throw new NotImplementedException(TaskAssignmentService.class, "assignNewTask");
    }
}
