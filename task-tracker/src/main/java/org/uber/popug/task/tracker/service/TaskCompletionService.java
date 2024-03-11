package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletionPublic;

public interface TaskCompletionService {
    void completeTask(TaskForCompletionPublic task);
}
