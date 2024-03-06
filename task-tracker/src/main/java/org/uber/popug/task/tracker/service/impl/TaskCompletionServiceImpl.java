package org.uber.popug.task.tracker.service.impl;

import org.uber.popug.task.tracker.domain.task.TaskForCompletion;
import org.uber.popug.task.tracker.exception.NotImplementedException;
import org.uber.popug.task.tracker.service.TaskCompletionService;

public class TaskCompletionServiceImpl implements TaskCompletionService {
    @Override
    public void completeTask(TaskForCompletion task) {
        throw new NotImplementedException(TaskCompletionServiceImpl.class, "completeTask");
    }
}
