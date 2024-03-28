package org.uber.popug.task.tracker.exception;

import lombok.Getter;
import org.uber.popug.task.tracker.validation.TaskCreationValidationViolation;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class JiraIdFormatException extends RuntimeException {

    private static final String JIRA_ID_FORMAT_VIOLATIONS_MESSAGE_TEMPLATE
            = "Error: invalid task provided for creation. Violations: %s";

    private final Collection<TaskCreationValidationViolation> taskCreationValidationViolations;

    public JiraIdFormatException(Collection<TaskCreationValidationViolation> taskCreationValidationViolations) {
        super(JIRA_ID_FORMAT_VIOLATIONS_MESSAGE_TEMPLATE.formatted(
                taskCreationValidationViolations.stream()
                        .map(TaskCreationValidationViolation::description)
                        .collect(Collectors.joining(System.lineSeparator()))
        ));

        this.taskCreationValidationViolations = taskCreationValidationViolations;
    }

}
