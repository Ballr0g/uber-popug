package org.uber.popug.task.tracker.domain.task;

import jakarta.annotation.Nonnull;

public record TaskForCreation(
        @Nonnull String taskDescription
) {
}
