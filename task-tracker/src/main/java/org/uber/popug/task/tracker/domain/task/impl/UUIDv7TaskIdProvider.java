package org.uber.popug.task.tracker.domain.task.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import org.uber.popug.task.tracker.domain.task.TaskIdProvider;

import java.util.UUID;

public class UUIDv7TaskIdProvider implements TaskIdProvider {

    @Override
    public UUID generateTaskId() {
        return UuidCreator.getTimeOrderedEpoch();
    }

}
