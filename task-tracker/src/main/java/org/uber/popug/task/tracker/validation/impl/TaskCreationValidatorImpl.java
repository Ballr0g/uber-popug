package org.uber.popug.task.tracker.validation.impl;

import org.uber.popug.task.tracker.domain.task.creation.TaskForCreation;
import org.uber.popug.task.tracker.validation.TaskCreationValidationViolation;
import org.uber.popug.task.tracker.validation.TaskCreationValidator;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class TaskCreationValidatorImpl implements TaskCreationValidator {

    private static final String JIRA_ID_IN_SQUARE_BRACKETS_IN_THE_BEGINNING_REGEX = "^\\[.*?]";
    private static final Pattern JIRA_ID_IN_DESCRIPTION_PATTERN
            = Pattern.compile(JIRA_ID_IN_SQUARE_BRACKETS_IN_THE_BEGINNING_REGEX);
    private static final String JIRA_ID_IN_SQUARE_BRACKETS_REGEX = "^\\[.*]$";
    private static final Pattern JIRA_ID_FORMAT_PATTERN = Pattern.compile(JIRA_ID_IN_SQUARE_BRACKETS_REGEX);

    @Override
    public Set<TaskCreationValidationViolation> validateTaskForCreationV2(TaskForCreation taskForCreation) {
        final var validationViolations = new HashSet<TaskCreationValidationViolation>();
        final var description = taskForCreation.description();
        final var jiraId = taskForCreation.jiraId();

        if (descriptionContainsJiraId(description)) {
            validationViolations.add(TaskCreationValidationViolation.forJiraIdInDescription(description));
        }
        if (jiraId == null) {
            validationViolations.add(TaskCreationValidationViolation.forNullJiraId());
        }
        else if (jiraIdFormatInvalid(jiraId)) {
            validationViolations.add(TaskCreationValidationViolation.forJiraIdViolation(jiraId));
        }

        return validationViolations;
    }

    private static boolean descriptionContainsJiraId(String description) {
        final var matcher = JIRA_ID_IN_DESCRIPTION_PATTERN.matcher(description);
        return matcher.find();
    }

    private static boolean jiraIdFormatInvalid(String jiraId) {
        final var matcher = JIRA_ID_FORMAT_PATTERN.matcher(jiraId);
        return !matcher.matches();
    }
}
