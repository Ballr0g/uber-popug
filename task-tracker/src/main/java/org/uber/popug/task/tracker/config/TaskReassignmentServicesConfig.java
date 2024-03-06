package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.service.TaskReassignmentService;
import org.uber.popug.task.tracker.service.impl.TaskReassignmentServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskReassignmentServicesConfig {

    @Bean
    public TaskReassignmentService taskReassignmentService() {
        return new TaskReassignmentServiceImpl();
    }

}
