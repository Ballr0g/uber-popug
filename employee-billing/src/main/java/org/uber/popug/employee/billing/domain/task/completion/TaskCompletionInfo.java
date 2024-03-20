package org.uber.popug.employee.billing.domain.task.completion;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCompletionInfo(
        @Nonnull UUID extPublicTaskId,
        @Nonnull UUID assigneeExtPublicId,
        @Nonnull String taskDescription,
        @Nonnull LocalDateTime completionDate
) {
}
