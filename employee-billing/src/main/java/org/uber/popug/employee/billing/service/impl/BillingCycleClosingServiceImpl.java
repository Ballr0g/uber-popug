package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycleIdProvider;
import org.uber.popug.employee.billing.exception.technical.NoActiveBillingCycleAvailableException;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;
import org.uber.popug.employee.billing.service.BillingCycleClosingService;

@RequiredArgsConstructor
public class BillingCycleClosingServiceImpl implements BillingCycleClosingService {

    private final BillingCycleRepository billingCycleRepository;
    private final BillingCycleIdProvider billingCycleIdProvider;
    private final BillingCyclesPersistenceMapper billingCyclesPersistenceMapper;

    @Override
    public BillingCycle closeActiveBillingCycle() {
        final var closedBillingCycleOpt = billingCycleRepository.closeActiveBillingCycle();
        if (closedBillingCycleOpt.isEmpty()) {
            throw NoActiveBillingCycleAvailableException.forNowUTC();
        }

        // Ideas:
        // 1) close transactions to assure no new operations are added, then change it to processed.
        // 2) Event inside app for billing cycle closing. @TransactionalEventListener/TransactionSynchronizationManager
        //    and TransactionSynchronization.
        // 3) commit hooks in Spring @Transactional.
        final var newBillingCycle = BillingCycle.createNewForNowUTC(billingCycleIdProvider);
        final var newBillingCycleEntity = billingCyclesPersistenceMapper.fromBusiness(newBillingCycle);
        billingCycleRepository.insertActiveBillingCycle(newBillingCycleEntity);
        return newBillingCycle;
    }

}
