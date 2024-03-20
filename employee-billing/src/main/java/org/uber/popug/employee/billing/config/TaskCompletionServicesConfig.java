package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.service.TaskCompletionService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;
import org.uber.popug.employee.billing.service.impl.TaskCompletionServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskCompletionServicesConfig {

    @Bean
    public TaskCompletionService taskCompletionService(
            TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper,
            UserAccountMembershipCheckingService userAccountMembershipCheckingService,
            UserAccountBillingService userAccountBillingService
    ) {
        return new TaskCompletionServiceImpl(
                tasksBusinessKafkaEventMapper,
                userAccountMembershipCheckingService,
                userAccountBillingService
        );
    }

}
