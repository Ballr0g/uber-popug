package org.uber.popug.task.tracker.domain.task.completion;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record TaskForCompletionPublic(
        @Nonnull UUID publicTaskId,
        @Nonnull UUID publicAssigneeId
) {
}
