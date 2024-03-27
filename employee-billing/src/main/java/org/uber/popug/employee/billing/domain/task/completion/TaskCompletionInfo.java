package org.uber.popug.employee.billing.domain.task.completion;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCompletionInfo(
        @Nonnull UUID taskExtPublicId,
        @Nonnull UUID assigneeExtPublicId,
        @Nullable String taskJiraId,
        @Nonnull String taskDescription,
        @Nonnull LocalDateTime completionDate
) {
}
