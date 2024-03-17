package org.uber.popug.employee.billing.domain.billing.creation;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskForBillingAssignment(
        @Nonnull UUID id,
        @Nonnull UUID assigneeId,
        @Nonnull LocalDateTime creationDate
) {
}
