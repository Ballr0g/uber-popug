package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.task.creation.TaskForCreation;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;

@Mapper
public interface TasksBusinessKafkaEventMapper {

    @Mapping(source = "taskId", target = "id")
    @Mapping(source = "taskDescription", target = "description")
    TaskForCreation toBusiness(TaskCreatedEvent taskCreatedEvent);

}
