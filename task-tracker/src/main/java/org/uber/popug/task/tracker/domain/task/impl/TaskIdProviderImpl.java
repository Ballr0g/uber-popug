package org.uber.popug.task.tracker.domain.task.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.domain.task.TaskIdProvider;
import org.uber.popug.task.tracker.repository.TaskRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class TaskIdProviderImpl implements TaskIdProvider {

    private final TaskRepository taskRepository;

    @Override
    public UUID generatePublicTaskId() {
        return UuidCreator.getTimeOrderedEpoch();
    }

    @Override
    public long generateDbTaskId() {
        return taskRepository.generateNextDbTaskId();
    }

}
