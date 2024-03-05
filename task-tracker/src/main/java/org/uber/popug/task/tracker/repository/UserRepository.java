package org.uber.popug.task.tracker.repository;

import org.uber.popug.task.tracker.entity.user.UserEntity;

import java.util.List;

public interface UserRepository {
    List<UserEntity> getDevelopers();
}
