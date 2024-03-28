package org.uber.popug.task.tracker.domain.task.creation;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record TaskForCreation(
        @Nullable String jiraId,
        @Nonnull String description
) {
}
