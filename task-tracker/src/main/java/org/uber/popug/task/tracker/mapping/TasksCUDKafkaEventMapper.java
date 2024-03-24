package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.event.cud.TaskCreatedReplicationEvent;

@Mapper
public interface TasksCUDKafkaEventMapper {

    @Mapping(source = "publicId", target = "taskId")
    @Mapping(source = "assignee.extPublicId", target = "assigneeId")
    @Mapping(source = "description", target = "taskDescription")
    @Mapping(
            target = "creationDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskCreatedReplicationEvent taskCreatedReplicationEventFromBusiness(Task task);

}
