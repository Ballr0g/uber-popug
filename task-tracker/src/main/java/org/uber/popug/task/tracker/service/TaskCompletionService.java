package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.domain.task.TaskForCompletion;

public interface TaskCompletionService {
    void completeTask(TaskForCompletion task);
}
