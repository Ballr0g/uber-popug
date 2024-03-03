package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.service.TaskAssignmentService;
import org.uber.popug.task.tracker.service.TaskTrackerService;
import org.uber.popug.task.tracker.service.impl.TaskAssignmentServiceImpl;
import org.uber.popug.task.tracker.service.impl.TaskTrackerServiceImpl;

@Configuration(proxyBeanMethods = false)
public class TaskServicesConfig {

    @Bean
    public TaskAssignmentService taskAssignmentService() {
        return new TaskAssignmentServiceImpl();
    }

    @Bean
    public TaskTrackerService taskTrackerService(
            TasksDtoMapper tasksDtoMapper,
            TaskAssignmentService taskAssignmentService
    ) {
        return new TaskTrackerServiceImpl(tasksDtoMapper, taskAssignmentService);
    }

}
