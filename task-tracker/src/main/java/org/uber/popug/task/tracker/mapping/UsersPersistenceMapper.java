package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.uber.popug.task.tracker.domain.task.TaskAssignee;
import org.uber.popug.task.tracker.entity.user.UserEntity;

@Mapper
public interface UsersPersistenceMapper {

    TaskAssignee userEntityToTaskAssignee(UserEntity userEntity);

}
