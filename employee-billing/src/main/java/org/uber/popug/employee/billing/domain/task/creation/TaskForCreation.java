package org.uber.popug.employee.billing.domain.task.creation;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskForCreation(
        @Nonnull UUID id,
        @Nonnull UUID assigneeId,
        @Nonnull String description,
        @Nonnull LocalDateTime creationDate
) {
}
