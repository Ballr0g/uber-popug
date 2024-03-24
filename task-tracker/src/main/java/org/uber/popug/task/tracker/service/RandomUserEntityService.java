package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.entity.user.UserEntity;

import java.util.List;

public interface RandomUserEntityService {
    UserEntity getRandomUserEntityFromList(List<UserEntity> userEntities);
}
