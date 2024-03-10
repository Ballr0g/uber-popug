package org.uber.popug.task.tracker.kafka.producer.event.cud;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.uber.popug.task.tracker.kafka.producer.event.EventBase;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TaskCreatedReplicationEvent extends EventBase<String, Object> {

    private static final String TASK_CREATED_REPLICATION_EVENT_NAME = "task.entity.created";
    private static final String TASK_CREATED_REPLICATION_EVENT_VERSION = "1.0";

    @Nonnull
    private final UUID taskId;
    @Nonnull private final UUID assigneeId;
    @Nonnull private final String taskDescription;
    @Nonnull private final LocalDateTime creationDate;

    public TaskCreatedReplicationEvent(
            @Nonnull UUID taskId,
            @Nonnull UUID assigneeId,
            @Nonnull String taskDescription,
            @Nonnull LocalDateTime creationDate
    ) {
        super(TASK_CREATED_REPLICATION_EVENT_NAME, TASK_CREATED_REPLICATION_EVENT_VERSION);

        this.taskId = taskId;
        this.assigneeId = assigneeId;
        this.taskDescription = taskDescription;
        this.creationDate = creationDate;
    }

    @Override
    public String recordKey() {
        return null;
    }

    @Override
    public Object recordValue() {
        return this;
    }
}
