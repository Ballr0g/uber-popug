package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.task.creation.TaskAssignmentInfo;
import org.uber.popug.employee.billing.domain.task.completion.TaskCompletionInfo;
import org.uber.popug.employee.billing.domain.task.reassignment.TaskReassignmentInfo;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCompletedEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskReassignedEventV1;

@Mapper
public interface TasksBusinessKafkaEventMapper {

    @Mapping(source = "data.taskId", target = "taskExtPublicId")
    @Mapping(source = "data.assigneeId", target = "assigneeExtPublicId")
    @Mapping(source = "data.creationDate", target = "creationDate")
    TaskAssignmentInfo toBusiness(TaskCreatedEventV1 taskCreatedEventV1);

    @Mapping(source = "data.taskId", target = "taskExtPublicId")
    @Mapping(source = "data.previousAssigneeId", target = "previousAssigneeExtPublicId")
    @Mapping(source = "data.newAssigneeId", target = "newAssigneeExtPublicId")
    @Mapping(source = "data.taskDescription", target = "taskDescription")
    @Mapping(source = "data.reassignmentDate", target = "reassignmentDate")
    TaskReassignmentInfo toBusiness(TaskReassignedEventV1 taskReassignedEventV1);

    @Mapping(source = "data.taskId", target = "taskExtPublicId")
    @Mapping(source = "data.assigneeId", target = "assigneeExtPublicId")
    @Mapping(source = "data.taskDescription", target = "taskDescription")
    @Mapping(source = "data.completionDate", target = "completionDate")
    TaskCompletionInfo toBusiness(TaskCompletedEventV1 taskCompletedEventV1);

}
