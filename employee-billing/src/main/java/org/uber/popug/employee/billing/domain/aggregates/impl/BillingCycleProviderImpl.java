package org.uber.popug.employee.billing.domain.aggregates.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.BillingCycleProvider;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;
import org.uber.popug.employee.billing.exception.technical.NoActiveBillingCycleAvailableException;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;

@RequiredArgsConstructor
public class BillingCycleProviderImpl implements BillingCycleProvider {

    private final BillingCycleRepository billingCycleRepository;
    private final BillingCyclesPersistenceMapper billingCyclesPersistenceMapper;

    @Override
    public BillingCycle retrieveActiveBillingCycle() {
        final var activeBillingCycleEntity = billingCycleRepository.findActiveBillingCycle().orElseThrow(
                NoActiveBillingCycleAvailableException::forNowUTC
        );

        return billingCyclesPersistenceMapper.toBusiness(activeBillingCycleEntity);
    }

}
