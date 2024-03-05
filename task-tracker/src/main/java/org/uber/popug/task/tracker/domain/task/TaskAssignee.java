package org.uber.popug.task.tracker.domain.task;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record TaskAssignee(
        long assigneeId,
        @Nonnull UUID publicAssigneeId,
        @Nonnull String assigneeLogin
) {
}
