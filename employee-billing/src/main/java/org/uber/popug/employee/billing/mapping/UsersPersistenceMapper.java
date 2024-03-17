package org.uber.popug.employee.billing.mapping;

import org.mapstruct.Mapper;
import org.uber.popug.employee.billing.domain.user.User;
import org.uber.popug.employee.billing.entity.user.UserEntity;

@Mapper
public interface UsersPersistenceMapper {

    User toBusiness(UserEntity userEntity);

}
