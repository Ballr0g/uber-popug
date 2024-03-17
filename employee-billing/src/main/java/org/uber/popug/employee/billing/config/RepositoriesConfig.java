package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.repository.UserRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientTaskRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientUserRepository;

@Configuration(proxyBeanMethods = false)
public class RepositoriesConfig {

    @Bean
    public TaskRepository taskRepository(
            JdbcClient jdbcClient
    ) {
        return new JdbcClientTaskRepository(jdbcClient);
    }

    @Bean
    public UserRepository userRepository(
            JdbcClient jdbcClient
    ) {
        return new JdbcClientUserRepository(jdbcClient);
    }

}
