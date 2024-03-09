package org.uber.popug.task.tracker.kafka.producer.event.business;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.uber.popug.task.tracker.kafka.producer.event.EventBase;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TaskCreatedEvent extends EventBase<String, Object> {

    private static final String TASK_CREATED_EVENT_NAME = "task.created";
    private static final String TASK_CREATED_EVENT_VERSION = "1.0";

    @Nonnull private final UUID taskId;
    @Nonnull private final UUID assigneeId;
    @Nonnull private final String taskDescription;
    @Nonnull private final LocalDateTime creationDate;

    public TaskCreatedEvent(
            @Nonnull UUID taskId,
            @Nonnull UUID assigneeId,
            @Nonnull String taskDescription,
            @Nonnull LocalDateTime creationDate
    ) {
        super(TASK_CREATED_EVENT_NAME, TASK_CREATED_EVENT_VERSION);

        this.taskId = taskId;
        this.assigneeId = assigneeId;
        this.taskDescription = taskDescription;
        this.creationDate = creationDate;
    }

    @Override
    public String recordKey() {
        return getTaskId().toString();
    }

    @Override
    public Object recordValue() {
        return this;
    }

}
