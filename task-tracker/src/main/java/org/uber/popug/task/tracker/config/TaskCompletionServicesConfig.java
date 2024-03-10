package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.TaskCompletionService;
import org.uber.popug.task.tracker.service.TaskMembershipCheckingService;
import org.uber.popug.task.tracker.service.impl.TaskCompletionServiceImpl;
import org.uber.popug.task.tracker.service.impl.TaskMembershipCheckingServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskCompletionServicesConfig {

    @Bean
    public TaskMembershipCheckingService taskMembershipCheckingService(
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        return new TaskMembershipCheckingServiceImpl(taskRepository, userRepository);
    }

    @Bean
    public TaskCompletionService taskCompletionService(
            TaskMembershipCheckingService taskMembershipCheckingService,
            TaskRepository taskRepository,
            UserRepository userRepository,
            TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper,
            TasksBusinessEventProducer tasksBusinessEventProducer
    ) {
        return new TaskCompletionServiceImpl(
                taskMembershipCheckingService,
                taskRepository,
                userRepository,
                tasksBusinessKafkaEventMapper,
                tasksBusinessEventProducer
        );
    }
}
