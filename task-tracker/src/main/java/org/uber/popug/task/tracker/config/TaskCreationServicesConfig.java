package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.domain.task.TaskIdProvider;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.mapping.UsersPersistenceMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.RandomUserEntityService;
import org.uber.popug.task.tracker.service.TaskAssignmentService;
import org.uber.popug.task.tracker.service.TaskAddingService;
import org.uber.popug.task.tracker.service.impl.JavaRandomUserEntityService;
import org.uber.popug.task.tracker.service.impl.TaskAssignmentServiceImpl;
import org.uber.popug.task.tracker.service.impl.TaskAddingServiceImpl;
import org.uber.popug.task.tracker.validation.TaskCreationValidator;

import java.util.random.RandomGenerator;

@Configuration(proxyBeanMethods = false)
public class TaskCreationServicesConfig {

    @Bean
    public RandomUserEntityService randomUserEntityService(
            RandomGenerator randomGenerator
    ) {
        return new JavaRandomUserEntityService(randomGenerator);
    }

    @Bean
    public TaskAssignmentService taskAssignmentService(
            UserRepository userRepository,
            RandomUserEntityService randomUserEntityService,
            UsersPersistenceMapper usersPersistenceMapper,
            TaskIdProvider taskIdProvider
    ) {
        return new TaskAssignmentServiceImpl(
                userRepository,
                randomUserEntityService,
                usersPersistenceMapper,
                taskIdProvider
        );
    }

    @Bean
    public TaskAddingService taskTrackerService(
            TasksDtoMapper tasksDtoMapper,
            TaskAssignmentService taskAssignmentService,
            TaskRepository taskRepository,
            TasksBusinessEventProducer tasksBusinessEventProducer,
            TasksCUDEventProducer tasksCUDEventProducer,
            TaskCreationValidator taskCreationValidator
    ) {
        return new TaskAddingServiceImpl(
                tasksDtoMapper,
                taskAssignmentService,
                taskRepository,
                tasksBusinessEventProducer,
                tasksCUDEventProducer,
                taskCreationValidator
        );
    }

}
