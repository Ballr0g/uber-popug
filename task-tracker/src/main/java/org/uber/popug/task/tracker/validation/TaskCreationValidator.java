package org.uber.popug.task.tracker.validation;

import org.uber.popug.task.tracker.domain.task.creation.TaskForCreation;

import java.util.Set;

public interface TaskCreationValidator {

    Set<TaskCreationValidationViolation> validateTaskForCreationV2(TaskForCreation taskForCreation);

}
