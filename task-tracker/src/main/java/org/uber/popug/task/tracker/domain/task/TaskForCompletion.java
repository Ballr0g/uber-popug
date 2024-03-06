package org.uber.popug.task.tracker.domain.task;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record TaskForCompletion(
        @Nonnull UUID publicTaskId,
        @Nonnull UUID publicAssigneeId
) {
}
