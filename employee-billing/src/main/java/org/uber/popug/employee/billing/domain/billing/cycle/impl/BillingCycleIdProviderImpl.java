package org.uber.popug.employee.billing.domain.billing.cycle.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycleIdProvider;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class BillingCycleIdProviderImpl implements BillingCycleIdProvider {

    private final BillingCycleRepository billingCycleRepository;

    @Override
    public UUID generatePublicBillingCycleId() {
        return UuidCreator.getTimeOrderedEpoch();
    }

    @Override
    public long generateDbBillingCycleId() {
        return billingCycleRepository.generateNextDbBillingCycleId();
    }
}
