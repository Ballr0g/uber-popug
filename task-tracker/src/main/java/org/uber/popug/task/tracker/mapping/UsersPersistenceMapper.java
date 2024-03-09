package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.TaskAssignee;
import org.uber.popug.task.tracker.entity.user.UserEntity;

@Mapper
public interface UsersPersistenceMapper {

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "login", target = "login")
    @Mapping(source = "extPublicUserId", target = "publicId")
    TaskAssignee userEntityToTaskAssignee(UserEntity userEntity);

}
