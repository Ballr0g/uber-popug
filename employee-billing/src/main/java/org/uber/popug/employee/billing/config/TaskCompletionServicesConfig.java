package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.service.TaskCompletionService;
import org.uber.popug.employee.billing.service.TransactionalTaskCompletionService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;
import org.uber.popug.employee.billing.service.impl.TaskCompletionServiceImpl;
import org.uber.popug.employee.billing.service.impl.TransactionalTaskCompletionServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskCompletionServicesConfig {

    @Bean
    public TransactionalTaskCompletionService transactionalTaskCompletionService(
            UserAccountBillingService userAccountBillingService,
            TaskRepository taskRepository,
            TasksPersistenceMapper tasksPersistenceMapper
    ) {
        return new TransactionalTaskCompletionServiceImpl(
                userAccountBillingService,
                taskRepository,
                tasksPersistenceMapper
        );
    }

    @Bean
    public TaskCompletionService taskCompletionService(
            TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper,
            UserAccountMembershipCheckingService userAccountMembershipCheckingService,
            TransactionalTaskCompletionService transactionalTaskCompletionService
    ) {
        return new TaskCompletionServiceImpl(
                tasksBusinessKafkaEventMapper,
                userAccountMembershipCheckingService,
                transactionalTaskCompletionService
        );
    }

}
