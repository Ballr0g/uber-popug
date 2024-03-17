package org.uber.popug.employee.billing.domain.billing.operation.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperationIdProvider;
import org.uber.popug.employee.billing.repository.ImmutableBillingOperationsRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class BillingOperationIdProviderImpl implements BillingOperationIdProvider {

    private final ImmutableBillingOperationsRepository billingOperationsRepository;

    @Override
    public UUID generatePublicBillingOperationId() {
        return UuidCreator.getTimeOrderedEpoch();
    }

    @Override
    public long generateDbBillingOperationId() {
        return billingOperationsRepository.generateNextDbBillingOperationId();
    }

}
