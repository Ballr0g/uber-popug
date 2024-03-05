package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.domain.task.TaskIdProvider;
import org.uber.popug.task.tracker.domain.task.impl.UUIDv7TaskIdProvider;

@Configuration
public class TasksConfig {

    @Bean
    public TaskIdProvider taskIdProvider() {
        return new UUIDv7TaskIdProvider();
    }

}
