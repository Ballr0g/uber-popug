package org.uber.popug.employee.billing.domain.task.creation;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskAssignmentInfo(
        @Nonnull UUID taskExtPublicId,
        @Nonnull UUID assigneeExtPublicId,
        @Nonnull LocalDateTime creationDate
) {
}
