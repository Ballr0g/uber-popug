package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.task.Task;
import org.uber.popug.employee.billing.entity.composite.TaskToAssigneeEntity;
import org.uber.popug.employee.billing.entity.task.TaskEntity;

@Mapper
public interface TasksPersistenceMapper {

    @Mapping(source = "task.id", target = "id")
    @Mapping(source = "task.extPublicId", target = "extPublicId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "task.description", target = "description")
    @Mapping(source = "task.status", target = "status")
    @Mapping(source = "task.costs.assignmentCost", target = "assignmentCost")
    @Mapping(source = "task.costs.completionCost", target = "completionCost")
    TaskEntity fromBusiness(TaskWithAssignee taskWithAssignee);

    @Mapping(source = "taskAssignee.id", target = "assignee.id")
    @Mapping(source = "taskAssignee.extPublicId", target = "assignee.extPublicId")
    @Mapping(source = "taskAssignee.login", target = "assignee.login")
    @Mapping(source = "taskEntity", target = "task", qualifiedByName = "taskToAssigneeEntityToBusiness")
    TaskWithAssignee toBusiness(TaskToAssigneeEntity taskEntity);

    @Mapping(source = "assignmentCost", target = "costs.assignmentCost")
    @Mapping(source = "completionCost", target = "costs.completionCost")
    Task toBusiness(TaskEntity taskEntity);

    Task.Status toBusiness(TaskEntity.Status status);

    @Named("taskToAssigneeEntityToBusiness")
    @Mapping(source = "costs.assignmentCost", target = "assignmentCost")
    @Mapping(source = "costs.completionCost", target = "completionCost")
    default Task taskToAssigneeEntityToBusiness(TaskToAssigneeEntity taskEntity) {
        return new Task(
                taskEntity.task().id(),
                taskEntity.task().extPublicId(),
                taskEntity.task().description(),
                toBusiness(taskEntity.task().status()),
                new Task.Costs(taskEntity.task().assignmentCost(), taskEntity.task().completionCost())
        );
    }

}
