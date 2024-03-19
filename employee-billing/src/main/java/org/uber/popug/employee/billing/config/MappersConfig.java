package org.uber.popug.employee.billing.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.mapping.BillingAccountsPersistenceMapper;
import org.uber.popug.employee.billing.mapping.BillingCyclesPersistenceMapper;
import org.uber.popug.employee.billing.mapping.BillingOperationsPersistenceMapper;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksCUDKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.mapping.UsersPersistenceMapper;

@Configuration(proxyBeanMethods = false)
public class MappersConfig {

    @Bean
    public UsersPersistenceMapper usersPersistenceMapper() {
        return Mappers.getMapper(UsersPersistenceMapper.class);
    }

    @Bean
    public TasksPersistenceMapper tasksPersistenceMapper() {
        return Mappers.getMapper(TasksPersistenceMapper.class);
    }

    @Bean
    public BillingOperationsPersistenceMapper billingOperationsPersistenceMapper() {
        return Mappers.getMapper(BillingOperationsPersistenceMapper.class);
    }

    @Bean
    public BillingAccountsPersistenceMapper billingAccountsPersistenceMapper() {
        return Mappers.getMapper(BillingAccountsPersistenceMapper.class);
    }

    @Bean
    public BillingCyclesPersistenceMapper billingCyclesPersistenceMapper() {
        return Mappers.getMapper(BillingCyclesPersistenceMapper.class);
    }

    @Bean
    public TasksCUDKafkaEventMapper tasksCUDKafkaEventMapper() {
        return Mappers.getMapper(TasksCUDKafkaEventMapper.class);
    }

    @Bean
    public TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper() {
        return Mappers.getMapper(TasksBusinessKafkaEventMapper.class);
    }

}
