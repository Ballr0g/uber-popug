package org.uber.popug.employee.billing.domain.billing.cycle;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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

    public static BillingCycle createNewForNowUTC(BillingCycleIdProvider billingCycleIdProvider) {
        final var currentUTCDate = LocalDate.now(ZoneOffset.UTC);
        final var currentDateStart = currentUTCDate.atStartOfDay();
        final var currentDateEnd = currentUTCDate.atTime(LocalTime.MAX);

        return new BillingCycle(
                billingCycleIdProvider.generateDbBillingCycleId(),
                billingCycleIdProvider.generatePublicBillingCycleId(),
                currentDateStart,
                currentDateEnd,
                State.ACTIVE
        );
    }
}
