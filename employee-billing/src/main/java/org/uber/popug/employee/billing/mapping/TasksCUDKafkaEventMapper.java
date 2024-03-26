package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.task.replication.TaskReplicationInfo;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV1;

@Mapper
public interface TasksCUDKafkaEventMapper {

    @Mapping(source = "data.taskId", target = "id")
    @Mapping(source = "data.assigneeId", target = "assigneeId")
    @Mapping(source = "data.taskDescription", target = "description")
    @Mapping(source = "data.creationDate", target = "creationDate")
    TaskReplicationInfo toBusiness(TaskCreatedReplicationEventV1 taskCreatedReplicationEventV1);

}
