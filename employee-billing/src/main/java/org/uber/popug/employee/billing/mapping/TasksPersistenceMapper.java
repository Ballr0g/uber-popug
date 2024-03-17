package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.entity.task.TaskEntity;

@Mapper
public interface TasksPersistenceMapper {

    @Mapping(source = "task.id", target = "id")
    @Mapping(source = "task.extPublicId", target = "extPublicId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "task.description", target = "description")
    @Mapping(source = "task.status", target = "status")
    @Mapping(source = "task.assignmentCost", target = "assignmentCost")
    @Mapping(source = "task.completionCost", target = "completionCost")
    TaskEntity fromBusiness(TaskWithAssignee taskWithAssignee);

}
