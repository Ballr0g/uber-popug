package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;

public interface BillingCycleClosingService {


    BillingCycle closeActiveBillingCycle();

}
