package org.uber.popug.task.tracker.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;

@Configuration(proxyBeanMethods = false)
public class MappersConfig {

    @Bean
    public TasksDtoMapper tasksRequestDtoMapper() {
        return Mappers.getMapper(TasksDtoMapper.class);
    }

}
