package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.TaskAssignee;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;

@Mapper
public interface TasksPersistenceMapper {

    @Mapping(source = "taskEntity.id", target = "id")
    @Mapping(source = "taskEntity.publicId", target = "publicId")
    @Mapping(source = "taskEntity.description", target = "description")
    @Mapping(source = "taskEntity.status", target = "status")
    @Mapping(source = "assigneeEntity", target = "assignee")
    Task taskEntityToBusiness(TaskEntity taskEntity, UserEntity assigneeEntity);

    TaskAssignee userEntityToTaskAssignee(UserEntity assigneeEntity);

}
