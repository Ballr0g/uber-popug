package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.dto.TaskCreatedEvent;

@Mapper
public interface TasksKafkaEventMapper {

    @Mapping(source = "publicTaskId", target = "publicId")
    @Mapping(source = "assignee.publicAssigneeId", target = "publicAssigneeId")
    @Mapping(source = "description", target = "description")
    @Mapping(
            target = "creationDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskCreatedEvent toTaskCreatedEventFromBusiness(Task task);

}
