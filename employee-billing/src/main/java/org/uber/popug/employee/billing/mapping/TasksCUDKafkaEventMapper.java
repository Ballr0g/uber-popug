package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.task.TaskInfo;
import org.uber.popug.employee.billing.kafka.event.cud.TaskCreatedReplicationEvent;

@Mapper
public interface TasksCUDKafkaEventMapper {

    @Mapping(source = "taskId", target = "id")
    @Mapping(source = "taskDescription", target = "description")
    TaskInfo toBusiness(TaskCreatedReplicationEvent taskCreatedReplicationEvent);

}
