package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.TaskAssignee;
import org.uber.popug.task.tracker.entity.user.UserEntity;

@Mapper
public interface UsersPersistenceMapper {

    @Mapping(source = "extPublicUserId", target = "assigneeId")
    @Mapping(source = "login", target = "assigneeLogin")
    TaskAssignee userEntityToTaskAssignee(UserEntity userEntity);

}
