package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycleIdProvider;
import org.uber.popug.employee.billing.mapping.BillingAccountsPersistenceMapper;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;
import org.uber.popug.employee.billing.service.BillingCycleClosingService;
import org.uber.popug.employee.billing.service.PaymentService;
import org.uber.popug.employee.billing.service.impl.BillingCycleClosingServiceImpl;
import org.uber.popug.employee.billing.service.impl.PaymentServiceImpl;

@Configuration(proxyBeanMethods = false)
public class ScheduledPaymentConfig {

    @Bean
    public BillingCycleClosingService billingCycleClosingService(
            BillingCycleRepository billingCycleRepository,
            BillingCycleIdProvider billingCycleIdProvider,
            BillingCyclesPersistenceMapper billingCyclesPersistenceMapper
    ) {
        return new BillingCycleClosingServiceImpl(
                billingCycleRepository,
                billingCycleIdProvider,
                billingCyclesPersistenceMapper
        );
    }

    @Bean
    public PaymentService paymentService(
            BillingAccountRepository billingAccountRepository,
            BillingAccountsPersistenceMapper billingAccountsPersistenceMapper
    ) {
        return new PaymentServiceImpl(billingAccountRepository, billingAccountsPersistenceMapper);
    }

}
