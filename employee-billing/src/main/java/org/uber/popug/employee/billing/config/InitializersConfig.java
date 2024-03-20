package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycleIdProvider;
import org.uber.popug.employee.billing.domain.billing.cycle.impl.BillingCycleIdProviderImpl;
import org.uber.popug.employee.billing.initialization.BillingCycleInitializer;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;

@Configuration(proxyBeanMethods = false)
public class InitializersConfig {

    @Bean
    public BillingCycleIdProvider billingCycleIdProvider(BillingCycleRepository billingCycleRepository) {
        return new BillingCycleIdProviderImpl(billingCycleRepository);
    }

    @Bean
    public BillingCycleInitializer billingCycleInitializer(
            BillingCycleRepository billingCycleRepository,
            BillingCycleIdProvider billingCycleIdProvider,
            BillingCyclesPersistenceMapper billingCyclesPersistenceMapper
    ) {
        return new BillingCycleInitializer(
                billingCycleRepository,
                billingCycleIdProvider,
                billingCyclesPersistenceMapper
        );
    }

}
