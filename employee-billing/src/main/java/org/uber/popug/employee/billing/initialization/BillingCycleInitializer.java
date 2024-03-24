package org.uber.popug.employee.billing.initialization;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycleIdProvider;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;

@RequiredArgsConstructor
public class BillingCycleInitializer implements ApplicationRunner {

    private final BillingCycleRepository billingCycleRepository;
    private final BillingCycleIdProvider billingCycleIdProvider;
    private final BillingCyclesPersistenceMapper billingCyclesPersistenceMapper;

    @Override
    public void run(ApplicationArguments args) {
        if (billingCycleRepository.findActiveBillingCycle().isPresent()) {
            return;
        }

        final var initialBillingCycle = BillingCycle.createNewForNowUTC(billingCycleIdProvider);
        final var initialBillingCycleEntity = billingCyclesPersistenceMapper.fromBusiness(initialBillingCycle);
        billingCycleRepository.insertActiveBillingCycle(initialBillingCycleEntity);
    }
}
