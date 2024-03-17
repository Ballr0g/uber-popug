package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientTaskRepository;

@Configuration(proxyBeanMethods = false)
public class RepositoriesConfig {

    @Bean
    public TaskRepository taskRepository(
            JdbcClient jdbcClient
    ) {
        return new JdbcClientTaskRepository(jdbcClient);
    }

}
