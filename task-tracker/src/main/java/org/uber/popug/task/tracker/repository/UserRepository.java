package org.uber.popug.task.tracker.repository;

import org.uber.popug.task.tracker.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    List<UserEntity> getDevelopers();

    Optional<UserEntity> findByPublicId(UUID publicUserId);

    Optional<UserEntity> findById(long userId);

}
