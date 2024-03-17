package org.uber.popug.employee.billing.domain.aggregates;

import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;

public interface BillingCycleProvider {

    BillingCycle retrieveActiveBillingCycle();

}
