package org.uber.popug.employee.billing.domain.billing.cycle;

import java.util.UUID;

public interface BillingCycleIdProvider {

    UUID generatePublicBillingCycleId();

    long generateDbBillingCycleId();

}
