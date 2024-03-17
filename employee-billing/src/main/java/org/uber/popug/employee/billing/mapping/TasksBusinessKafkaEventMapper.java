package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.billing.creation.TaskForBillingAssignment;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;

@Mapper
public interface TasksBusinessKafkaEventMapper {

    @Mapping(source = "taskId", target = "id")
    TaskForBillingAssignment toBusiness(TaskCreatedEvent taskCreatedEvent);

}
