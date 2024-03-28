package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.validation.TaskCreationValidator;
import org.uber.popug.task.tracker.validation.impl.TaskCreationValidatorImpl;

@Configuration(proxyBeanMethods = false)
public class ValidatorsConfig {

    @Bean
    public TaskCreationValidator taskCreationValidator() {
        return new TaskCreationValidatorImpl();
    }

}
