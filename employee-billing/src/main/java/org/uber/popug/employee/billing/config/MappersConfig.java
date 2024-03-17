package org.uber.popug.employee.billing.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.employee.billing.mapping.TasksCUDKafkaEventMapper;

@Configuration(proxyBeanMethods = false)
public class MappersConfig {

    @Bean
    public TasksCUDKafkaEventMapper tasksCUDKafkaEventMapper() {
        return Mappers.getMapper(TasksCUDKafkaEventMapper.class);
    }

}
