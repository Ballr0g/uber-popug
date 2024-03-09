package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.mapping.TasksKafkaEventMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.RandomUserEntityService;
import org.uber.popug.task.tracker.service.TaskReassignmentService;
import org.uber.popug.task.tracker.service.impl.TaskReassignmentServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskReassignmentServicesConfig {

    @Bean
    public TaskReassignmentService taskReassignmentService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            RandomUserEntityService randomUserEntityService,
            TasksKafkaEventMapper tasksKafkaEventMapper,
            TasksBusinessEventProducer tasksBusinessEventProducer
    ) {
        return new TaskReassignmentServiceImpl(
                taskRepository,
                userRepository,
                randomUserEntityService,
                tasksKafkaEventMapper,
                tasksBusinessEventProducer
        );
    }

}
