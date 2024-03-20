package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.domain.aggregates.BillingCycleProvider;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.aggregates.impl.BillingCycleProviderImpl;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperationDescriptionBuilder;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperationIdProvider;
import org.uber.popug.employee.billing.domain.billing.operation.impl.BillingOperationIdProviderImpl;
import org.uber.popug.employee.billing.domain.billing.operation.impl.TaskAssignmentBillingOperationDescriptionBuilder;
import org.uber.popug.employee.billing.domain.billing.operation.impl.TaskReassignmentBillingOperationDescriptionBuilder;
import org.uber.popug.employee.billing.mapping.BillingAccountsPersistenceMapper;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.mapping.BillingOperationsPersistenceMapper;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.mapping.UsersPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;
import org.uber.popug.employee.billing.repository.ImmutableBillingOperationsRepository;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.repository.UserRepository;
import org.uber.popug.employee.billing.service.BillingAccountManagementService;
import org.uber.popug.employee.billing.service.BillingOperationLogService;
import org.uber.popug.employee.billing.service.TaskAssignmentService;
import org.uber.popug.employee.billing.service.TaskBillingOperationAssemblingService;
import org.uber.popug.employee.billing.service.TransactionalAccountingService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;
import org.uber.popug.employee.billing.service.impl.BillingAccountManagementServiceImpl;
import org.uber.popug.employee.billing.service.impl.BillingOperationLogServiceImpl;
import org.uber.popug.employee.billing.service.impl.TaskAssignmentServiceImpl;
import org.uber.popug.employee.billing.service.impl.TaskBillingOperationAssemblingServiceImpl;
import org.uber.popug.employee.billing.service.impl.TransactionalAccountingServiceImpl;
import org.uber.popug.employee.billing.service.impl.UserAccountBillingServiceImpl;
import org.uber.popug.employee.billing.service.impl.UserAccountMembershipCheckingServiceImpl;

@Configuration(proxyBeanMethods = false)
public class BillingServicesConfig {

    @Bean
    public BillingOperationIdProvider billingOperationIdProvider(
            ImmutableBillingOperationsRepository billingOperationsRepository
    ) {
        return new BillingOperationIdProviderImpl(billingOperationsRepository);
    }

    @Bean
    public BillingOperationDescriptionBuilder<TaskWithAssignee> newlyAssignedTaskDescriptionBuilder() {
        return new TaskAssignmentBillingOperationDescriptionBuilder();
    }

    @Bean
    public BillingOperationDescriptionBuilder<TaskWithAssignee> reassignedTaskDescriptionBuilder() {
        return new TaskReassignmentBillingOperationDescriptionBuilder();
    }

    @Bean
    public TaskBillingOperationAssemblingService taskBillingOperationAssemblingService(
            BillingOperationIdProvider billingOperationIdProvider,
            BillingOperationDescriptionBuilder<TaskWithAssignee> newlyAssignedTaskDescriptionBuilder,
            BillingOperationDescriptionBuilder<TaskWithAssignee> reassignedTaskDescriptionBuilder
    ) {
        return new TaskBillingOperationAssemblingServiceImpl(
                billingOperationIdProvider,
                newlyAssignedTaskDescriptionBuilder,
                reassignedTaskDescriptionBuilder
        );
    }

    @Bean
    public BillingCycleProvider billingCycleProvider(
            BillingCycleRepository billingCycleRepository,
            BillingCyclesPersistenceMapper billingCyclesPersistenceMapper
    ) {
        return new BillingCycleProviderImpl(billingCycleRepository, billingCyclesPersistenceMapper);
    }

    @Bean
    public BillingOperationLogService billingOperationLogService(
            TaskBillingOperationAssemblingService billingOperationAssemblingService,
            BillingCycleProvider billingCycleProvider,
            BillingOperationsPersistenceMapper billingOperationsPersistenceMapper,
            ImmutableBillingOperationsRepository billingOperationsRepository
    ) {
        return new BillingOperationLogServiceImpl(
                billingOperationAssemblingService,
                billingCycleProvider,
                billingOperationsPersistenceMapper,
                billingOperationsRepository
        );
    }

    @Bean
    public UserAccountMembershipCheckingService userAccountMembershipCheckingService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            TasksPersistenceMapper tasksPersistenceMapper,
            UsersPersistenceMapper usersPersistenceMapper
    ) {
        return new UserAccountMembershipCheckingServiceImpl(
                taskRepository,
                userRepository,
                tasksPersistenceMapper,
                usersPersistenceMapper
        );
    }

    @Bean
    public BillingAccountManagementService billingAccountManagementService(
            BillingAccountRepository billingAccountRepository,
            BillingAccountsPersistenceMapper billingAccountsPersistenceMapper
    ) {
        return new BillingAccountManagementServiceImpl(billingAccountRepository, billingAccountsPersistenceMapper);
    }

    @Bean
    public TransactionalAccountingService transactionalAccountingService(
            BillingOperationLogService billingOperationLogService,
            BillingAccountManagementService billingAccountManagementService
    ) {
        return new TransactionalAccountingServiceImpl(
                billingOperationLogService,
                billingAccountManagementService
        );
    }

    @Bean
    public UserAccountBillingService userAccountBillingService(
            TransactionalAccountingService transactionalAccountingService
    ) {
        return new UserAccountBillingServiceImpl(transactionalAccountingService);
    }

    @Bean
    public TaskAssignmentService taskAssignmentService(
            TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper,
            UserAccountMembershipCheckingService accountMembershipCheckingService,
            UserAccountBillingService userAccountBillingService
    ) {
        return new TaskAssignmentServiceImpl(
                tasksBusinessKafkaEventMapper,
                accountMembershipCheckingService,
                userAccountBillingService
        );
    }

}
