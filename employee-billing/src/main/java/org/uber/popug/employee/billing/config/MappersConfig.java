package org.uber.popug.employee.billing.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.mapping.TasksCUDKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.mapping.UsersPersistenceMapper;

@Configuration(proxyBeanMethods = false)
public class MappersConfig {

    @Bean
    public TasksCUDKafkaEventMapper tasksCUDKafkaEventMapper() {
        return Mappers.getMapper(TasksCUDKafkaEventMapper.class);
    }

    @Bean
    public UsersPersistenceMapper usersPersistenceMapper() {
        return Mappers.getMapper(UsersPersistenceMapper.class);
    }

    @Bean
    TasksPersistenceMapper tasksPersistenceMapper() {
        return Mappers.getMapper(TasksPersistenceMapper.class);
    }

}
