package org.uber.popug.task.tracker.domain.task;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record TaskAssignee(
        long id,
        @Nonnull UUID publicId,
        @Nonnull String login
) {
}
