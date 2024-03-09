package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCompletedEvent;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCreatedEvent;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskReassignedEvent;

@Mapper
public interface TasksKafkaEventMapper {

    @Mapping(source = "publicId", target = "taskId")
    @Mapping(source = "assignee.extPublicId", target = "assigneeId")
    @Mapping(source = "description", target = "taskDescription")
    @Mapping(
            target = "creationDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskCreatedEvent toTaskCreatedEventFromBusiness(Task task);

    @Mapping(source = "task.publicId", target = "taskId")
    @Mapping(source = "previousAssignee.extPublicId", target = "previousAssigneeId")
    @Mapping(source = "newAssignee.extPublicId", target = "newAssigneeId")
    @Mapping(source = "task.description", target = "taskDescription")
    @Mapping(
            target = "reassignmentDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskReassignedEvent toTaskReassignedEventFromBusiness(TaskEntity task, UserEntity previousAssignee, UserEntity newAssignee);

    @Mapping(source = "task.publicId", target = "taskId")
    @Mapping(source = "assignee.extPublicId", target = "assigneeId")
    @Mapping(source = "task.description", target = "taskDescription")
    @Mapping(
            target = "completionDate",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"UTC\")).toLocalDateTime())"
    )
    TaskCompletedEvent toTaskCompletedEventFromEntities(TaskEntity task, UserEntity assignee);

}
