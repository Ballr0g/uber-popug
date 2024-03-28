package org.uber.popug.task.tracker.validation;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

public record TaskCreationValidationViolation(
        @Nonnull String description,
        @Nonnull Type type
) {

    private static final String JIRA_ID_MISSING_MESSAGE
            = "Jira ID required but missing in the actual request.";

    private static final String JIRA_ID_VIOLATION_MESSAGE_TEMPLATE
            = "Illegal jira ID: received %s, expected a Jira ID formatted as: [<Code>]";

    private static final String JIRA_ID_IN_DESCRIPTION_VIOLATION_MESSAGE_TEMPLATE
            = "Illegal description: received %s, expected description not to contain a Jira ID.";

    @RequiredArgsConstructor
    public enum Type {
        MISSING_JIRA_ID,
        ILLEGAL_JIRA_ID_FORMAT,
        JIRA_ID_IN_DESCRIPTION
    }

    public static TaskCreationValidationViolation forJiraIdViolation(String jiraId) {
        return new TaskCreationValidationViolation(
                JIRA_ID_VIOLATION_MESSAGE_TEMPLATE.formatted(jiraId),
                Type.ILLEGAL_JIRA_ID_FORMAT
        );
    }

    public static TaskCreationValidationViolation forNullJiraId() {
        return new TaskCreationValidationViolation(JIRA_ID_MISSING_MESSAGE, Type.MISSING_JIRA_ID);
    }

    public static TaskCreationValidationViolation forJiraIdInDescription(String description) {
        return new TaskCreationValidationViolation(
                JIRA_ID_IN_DESCRIPTION_VIOLATION_MESSAGE_TEMPLATE.formatted(description),
                Type.JIRA_ID_IN_DESCRIPTION
        );
    }
}
