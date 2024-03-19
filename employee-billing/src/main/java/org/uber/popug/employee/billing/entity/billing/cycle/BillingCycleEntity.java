package org.uber.popug.employee.billing.entity.billing.cycle;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public record BillingCycleEntity(
        long id,
        @Nonnull UUID publicId,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate,
        @Nonnull State state
) {
    @Getter
    @RequiredArgsConstructor
    public enum State {
        ACTIVE("ACTIVE"),
        CLOSED("CLOSED");

        @Nonnull
        private final String persistenceValue;
    }
}
