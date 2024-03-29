package org.uber.popug.task.tracker.domain.task;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record Task(
        long id,
        @Nonnull UUID publicId,
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
            @Nonnull String description,
            @Nonnull TaskAssignee assignee
    ) {
        return new Task(
                taskIdProvider.generateDbTaskId(),
                taskIdProvider.generatePublicTaskId(),
                description,
                Status.OPEN,
                assignee
        );
    }

}
