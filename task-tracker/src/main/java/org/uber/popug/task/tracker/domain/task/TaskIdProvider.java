package org.uber.popug.task.tracker.domain.task;

import java.util.UUID;

public interface TaskIdProvider {
    UUID generateTaskId();
}
