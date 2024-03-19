package org.uber.popug.employee.billing.exception.technical;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
public class NoActiveBillingCycleAvailableException extends RuntimeException {

    private static final String NO_ACTIVE_BILLING_CYCLE_AVAILABLE_MESSAGE_TEMPLATE
            = "There were no active billing cycles found at the time of request: %s";

    private final LocalDateTime billingCycleUnavailabilityTime;

    public NoActiveBillingCycleAvailableException(LocalDateTime billingCycleUnavailabilityTime) {
        super(NO_ACTIVE_BILLING_CYCLE_AVAILABLE_MESSAGE_TEMPLATE.formatted(billingCycleUnavailabilityTime));

        this.billingCycleUnavailabilityTime = billingCycleUnavailabilityTime;
    }

    public static NoActiveBillingCycleAvailableException forNowUTC() {
        return new NoActiveBillingCycleAvailableException(LocalDateTime.now(ZoneOffset.UTC));
    }
}
