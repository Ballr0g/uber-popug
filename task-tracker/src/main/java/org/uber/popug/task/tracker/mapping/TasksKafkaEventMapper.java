package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.kafka.producer.dto.TaskCompletedEvent;
import org.uber.popug.task.tracker.kafka.producer.dto.TaskCreatedEvent;
import org.uber.popug.task.tracker.kafka.producer.dto.TaskReassignedEvent;

@Mapper
public interface TasksKafkaEventMapper {

    @Mapping(source = "publicId", target = "publicId")
    @Mapping(source = "assignee.extPublicId", target = "publicAssigneeId")
    @Mapping(source = "description", target = "description")
    @Mapping(
            target = "creationDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskCreatedEvent toTaskCreatedEventFromBusiness(Task task);

    @Mapping(source = "task.publicId", target = "publicId")
    @Mapping(source = "previousAssignee.extPublicId", target = "previousAssigneePublicId")
    @Mapping(source = "newAssignee.extPublicId", target = "newAssigneePublicId")
    @Mapping(source = "task.description", target = "description")
    @Mapping(
            target = "reassignmentDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskReassignedEvent toTaskReassignedEventFromBusiness(TaskEntity task, UserEntity previousAssignee, UserEntity newAssignee);

    @Mapping(source = "task.publicId", target = "publicId")
    @Mapping(source = "assignee.extPublicId", target = "extPublicAssigneeId")
    @Mapping(source = "task.description", target = "description")
    @Mapping(
            target = "completionDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskCompletedEvent toTaskCompletedEventFromEntities(TaskEntity task, UserEntity assignee);

}
