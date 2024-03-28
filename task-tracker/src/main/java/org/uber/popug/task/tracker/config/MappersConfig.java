package org.uber.popug.task.tracker.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.task.tracker.mapping.TasksPersistenceMapper;
import org.uber.popug.task.tracker.mapping.UsersPersistenceMapper;

@Configuration(proxyBeanMethods = false)
public class MappersConfig {

    @Bean
    public TasksDtoMapper tasksRequestDtoMapper() {
        return Mappers.getMapper(TasksDtoMapper.class);
    }

    @Bean
    public UsersPersistenceMapper usersPersistenceMapper() {
        return Mappers.getMapper(UsersPersistenceMapper.class);
    }

    @Bean
    public TasksPersistenceMapper tasksPersistenceMapper() {
        return Mappers.getMapper(TasksPersistenceMapper.class);
    }

    @Bean
    public TasksBusinessKafkaEventMapper tasksKafkaEventMapper() {
        return Mappers.getMapper(TasksBusinessKafkaEventMapper.class);
    }

}
