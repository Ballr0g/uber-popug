package org.uber.popug.employee.billing.repository;

import org.uber.popug.employee.billing.entity.billing.cycle.BillingCycleEntity;

import java.util.Optional;

public interface BillingCycleRepository {

    long generateNextDbBillingCycleId();

    int insertActiveBillingCycle(BillingCycleEntity billingCycleEntity);

    Optional<BillingCycleEntity> findActiveBillingCycle();

}
