package org.uber.popug.employee.billing.domain.billing.cycle;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public record BillingCycle(
        long id,
        @Nonnull UUID publicId,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate,
        @Nonnull State state
) {
    @Getter
    @RequiredArgsConstructor
    public enum State {
        ACTIVE,
        CLOSED
    }
}
