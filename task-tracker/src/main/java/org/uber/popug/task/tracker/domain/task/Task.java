package org.uber.popug.task.tracker.domain.task;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.uber.popug.task.tracker.domain.task.creation.TaskForCreation;

import java.util.UUID;

public record Task(
        long id,
        @Nonnull UUID publicId,
        @Nullable String jiraId,
        @Nonnull String description,
        @Nonnull Status status,
        @Nonnull TaskAssignee assignee
) {
    public enum Status {
        OPEN,
        COMPLETED
    }

    public static Task create(
            @Nonnull TaskIdProvider taskIdProvider,
            @Nonnull TaskForCreation taskForCreation,
            @Nonnull TaskAssignee assignee
    ) {
        return new Task(
                taskIdProvider.generateDbTaskId(),
                taskIdProvider.generatePublicTaskId(),
                taskForCreation.jiraId(),
                taskForCreation.description(),
                Status.OPEN,
                assignee
        );
    }

}
