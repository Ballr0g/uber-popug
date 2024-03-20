package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.service.TaskReassignmentService;
import org.uber.popug.employee.billing.service.TransactionalTaskReassignmentService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;
import org.uber.popug.employee.billing.service.impl.TaskReassignmentServiceImpl;
import org.uber.popug.employee.billing.service.impl.TransactionalTaskReassignmentServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskReassignmentServicesConfig {

    @Bean
    public TransactionalTaskReassignmentService transactionalTaskReassignmentService(
            UserAccountBillingService userAccountBillingService,
            TaskRepository taskRepository,
            TasksPersistenceMapper tasksPersistenceMapper
    ) {
        return new TransactionalTaskReassignmentServiceImpl(
                userAccountBillingService,
                taskRepository,
                tasksPersistenceMapper
        );
    }

    @Bean
    public TaskReassignmentService taskReassignmentService(
            TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper,
            UserAccountMembershipCheckingService userAccountMembershipCheckingService,
            TransactionalTaskReassignmentService transactionalTaskReassignmentService
    ) {
        return new TaskReassignmentServiceImpl(
                tasksBusinessKafkaEventMapper,
                userAccountMembershipCheckingService,
                transactionalTaskReassignmentService
        );
    }

}
