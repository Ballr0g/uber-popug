package org.uber.popug.task.tracker.domain.task;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record TaskAssignee(
        @Nonnull UUID assigneeId,
        @Nonnull String assigneeLogin
) {
}
