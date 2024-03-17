package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.domain.task.TaskCostsProvider;
import org.uber.popug.employee.billing.domain.task.TaskIdProvider;
import org.uber.popug.employee.billing.domain.task.impl.PostgresTaskIdProvider;
import org.uber.popug.employee.billing.domain.task.impl.RandomTaskCostsProvider;
import org.uber.popug.employee.billing.mapping.TasksCUDKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.mapping.UsersPersistenceMapper;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.repository.UserRepository;
import org.uber.popug.employee.billing.service.TaskBillingAssignmentService;
import org.uber.popug.employee.billing.service.TaskBillingReplicationService;
import org.uber.popug.employee.billing.service.impl.TaskBillingAssignmentServiceImpl;
import org.uber.popug.employee.billing.service.impl.TaskBillingReplicationServiceImpl;

import java.util.Random;
import java.util.random.RandomGenerator;

@Configuration(proxyBeanMethods = false)
public class TaskReplicationServiceConfig {

    @Bean
    public RandomGenerator javaRandomGenerator() {
        return new Random();
    }

    @Bean
    public TaskCostsProvider taskCostsProvider(
            RandomGenerator javaRandomGenerator
    ) {
        return new RandomTaskCostsProvider(javaRandomGenerator);
    }

    @Bean
    public TaskIdProvider taskIdProvider(
            TaskRepository taskRepository
    ) {
        return new PostgresTaskIdProvider(taskRepository);
    }

    @Bean
    public TaskBillingAssignmentService taskBillingAssignmentService(
            UserRepository userRepository,
            UsersPersistenceMapper usersPersistenceMapper,
            TaskIdProvider taskIdProvider,
            TaskCostsProvider taskCostsProvider
    ) {
        return new TaskBillingAssignmentServiceImpl(
                userRepository,
                usersPersistenceMapper,
                taskIdProvider,
                taskCostsProvider
        );
    }

    @Bean
    public TaskBillingReplicationService taskBillingReplicationService(
            TasksCUDKafkaEventMapper tasksCUDKafkaEventMapper,
            TaskBillingAssignmentService taskBillingAssignmentService,
            TaskRepository taskRepository,
            TasksPersistenceMapper tasksPersistenceMapper
    ) {
        return new TaskBillingReplicationServiceImpl(
                tasksCUDKafkaEventMapper,
                taskBillingAssignmentService,
                taskRepository,
                tasksPersistenceMapper
        );
    }

}
