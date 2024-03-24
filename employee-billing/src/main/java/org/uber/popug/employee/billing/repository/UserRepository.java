package org.uber.popug.employee.billing.repository;

import org.uber.popug.employee.billing.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<UserEntity> findByPublicId(UUID publicUserId);

}
