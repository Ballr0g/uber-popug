package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.task.creation.TaskAssignmentInfo;
import org.uber.popug.employee.billing.domain.task.completion.TaskCompletionInfo;
import org.uber.popug.employee.billing.domain.task.reassignment.TaskReassignmentInfo;
import org.uber.popug.employee.billing.kafka.event.business.TaskCompletedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskReassignedEvent;

@Mapper
public interface TasksBusinessKafkaEventMapper {

    @Mapping(source = "taskId", target = "taskExtPublicId")
    @Mapping(source = "assigneeId", target = "assigneeExtPublicId")
    TaskAssignmentInfo toBusiness(TaskCreatedEvent taskCreatedEvent);

    @Mapping(source = "taskId", target = "taskExtPublicId")
    @Mapping(source = "previousAssigneeId", target = "previousAssigneeExtPublicId")
    @Mapping(source = "newAssigneeId", target = "newAssigneeExtPublicId")
    TaskReassignmentInfo toBusiness(TaskReassignedEvent taskReassignedEvent);

    @Mapping(source = "taskId", target = "taskExtPublicId")
    @Mapping(source = "assigneeId", target = "assigneeExtPublicId")
    TaskCompletionInfo toBusiness(TaskCompletedEvent taskCompletedEvent);

}
