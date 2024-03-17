package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.task.replication.TaskReplicationInfo;
import org.uber.popug.employee.billing.kafka.event.cud.TaskCreatedReplicationEvent;

@Mapper
public interface TasksCUDKafkaEventMapper {

    @Mapping(source = "taskId", target = "id")
    @Mapping(source = "taskDescription", target = "description")
    TaskReplicationInfo toBusiness(TaskCreatedReplicationEvent taskCreatedReplicationEvent);

}
