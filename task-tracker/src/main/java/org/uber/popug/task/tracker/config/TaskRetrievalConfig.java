package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.mapping.TasksPersistenceMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.TaskRetrievalService;
import org.uber.popug.task.tracker.service.impl.TaskRetrievalServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskRetrievalConfig {

    @Bean
    public TaskRetrievalService taskRetrievalService(
            UserRepository userRepository,
            TaskRepository taskRepository,
            TasksPersistenceMapper tasksPersistenceMapper
    ) {
        return new TaskRetrievalServiceImpl(userRepository, taskRepository, tasksPersistenceMapper);
    }

}
