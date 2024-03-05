package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.service.RandomUserEntityService;

import java.util.List;
import java.util.random.RandomGenerator;

@RequiredArgsConstructor
public class JavaRandomUserEntityService implements RandomUserEntityService {
    private final RandomGenerator randomGenerator;
    @Override
    public UserEntity getRandomUserEntityFromList(List<UserEntity> userEntities) {
        return userEntities.get(randomGenerator.nextInt(userEntities.size()));
    }
}
