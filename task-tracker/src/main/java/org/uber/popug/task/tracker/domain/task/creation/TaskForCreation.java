package org.uber.popug.task.tracker.domain.task.creation;

import jakarta.annotation.Nonnull;

public record TaskForCreation(
        @Nonnull String description
) {
}
