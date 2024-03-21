package org.uber.popug.employee.billing.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycleIdProvider;
import org.uber.popug.employee.billing.exception.technical.NoActiveBillingCycleAvailableException;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;

@Service
@RequiredArgsConstructor
public class BillingCycleManagementScheduler {

    private final BillingCycleRepository billingCycleRepository;
    private final BillingCycleIdProvider billingCycleIdProvider;
    private final BillingCyclesPersistenceMapper billingCyclesPersistenceMapper;

    @Transactional
    @Scheduled(cron = "${billing-cycles.closing-interval}", zone = "UTC")
    public void closeCurrentBillingCycle() {
        final var closedBillingCycleOpt = billingCycleRepository.closeActiveBillingCycle();
        if (closedBillingCycleOpt.isEmpty()) {
            throw NoActiveBillingCycleAvailableException.forNowUTC();
        }

        final var newBillingCycle = BillingCycle.createNewForNowUTC(billingCycleIdProvider);
        final var newBillingCycleEntity = billingCyclesPersistenceMapper.fromBusiness(newBillingCycle);
        billingCycleRepository.insertActiveBillingCycle(newBillingCycleEntity);
    }

}
