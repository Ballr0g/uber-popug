package org.uber.popug.employee.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;
import org.uber.popug.employee.billing.repository.ImmutableBillingOperationsRepository;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.repository.UserRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientBillingAccountRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientBillingCycleRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientImmutableBillingOperationsRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientTaskRepository;
import org.uber.popug.employee.billing.repository.impl.JdbcClientUserRepository;

@Configuration(proxyBeanMethods = false)
public class RepositoriesConfig {

    @Bean
    public TaskRepository taskRepository(JdbcClient jdbcClient) {
        return new JdbcClientTaskRepository(jdbcClient);
    }

    @Bean
    public UserRepository userRepository(JdbcClient jdbcClient) {
        return new JdbcClientUserRepository(jdbcClient);
    }

    @Bean
    public ImmutableBillingOperationsRepository billingOperationsRepository(JdbcClient jdbcClient) {
        return new JdbcClientImmutableBillingOperationsRepository(jdbcClient);
    }

    @Bean
    public BillingAccountRepository billingAccountRepository(JdbcClient jdbcClient) {
        return new JdbcClientBillingAccountRepository(jdbcClient);
    }

    @Bean
    public BillingCycleRepository billingCycleRepository(JdbcClient jdbcClient) {
        return new JdbcClientBillingCycleRepository(jdbcClient);
    }

}
