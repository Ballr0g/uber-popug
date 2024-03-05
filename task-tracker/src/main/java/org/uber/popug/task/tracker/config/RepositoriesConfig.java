package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.repository.impl.JdbcClientTaskRepository;
import org.uber.popug.task.tracker.repository.impl.JdbcClientUserRepository;

@Configuration(proxyBeanMethods = false)
public class RepositoriesConfig {

    @Bean
    public UserRepository userRepository(
            JdbcClient jdbcClient
    ) {
        return new JdbcClientUserRepository(jdbcClient);
    }

    @Bean
    public TaskRepository taskRepository(
            JdbcClient jdbcClient
    ) {
        return new JdbcClientTaskRepository(jdbcClient);
    }

}
