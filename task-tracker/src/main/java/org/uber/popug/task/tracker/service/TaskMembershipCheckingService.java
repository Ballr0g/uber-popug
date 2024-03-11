package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletion;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletionPublic;

import java.util.Optional;

public interface TaskMembershipCheckingService {

    Optional<TaskForCompletion> checkCompletionValid(TaskForCompletionPublic task);

}
