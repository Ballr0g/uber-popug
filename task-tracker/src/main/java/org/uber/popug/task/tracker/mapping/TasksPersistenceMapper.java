package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;

@Mapper
public interface TasksPersistenceMapper {

    @Mapping(source = "taskEntity.taskId", target = "taskId")
    @Mapping(source = "taskEntity.publicTaskId", target = "publicTaskId")
    @Mapping(source = "taskEntity.description", target = "description")
    @Mapping(source = "taskEntity.status", target = "status")
    @Mapping(source = "taskEntity.assigneeId", target = "assignee.assigneeId")
    @Mapping(source = "userEntity.extPublicUserId", target = "assignee.publicAssigneeId")
    @Mapping(source = "userEntity.login", target = "assignee.assigneeLogin")
    Task TaskEntityToBusiness(TaskEntity taskEntity, UserEntity assigneeEntity);

}
