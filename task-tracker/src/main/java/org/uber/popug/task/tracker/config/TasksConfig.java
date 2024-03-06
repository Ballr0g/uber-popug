package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.domain.task.TaskIdProvider;
import org.uber.popug.task.tracker.domain.task.impl.TaskIdProviderImpl;
import org.uber.popug.task.tracker.repository.TaskRepository;

@Configuration
public class TasksConfig {

    @Bean
    public TaskIdProvider taskIdProvider(
            TaskRepository taskRepository
    ) {
        return new TaskIdProviderImpl(taskRepository);
    }

}
