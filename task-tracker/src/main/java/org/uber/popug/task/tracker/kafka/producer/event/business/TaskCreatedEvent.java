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

    @Nonnull private final UUID publicId;
    @Nonnull private final UUID publicAssigneeId;
    @Nonnull private final String description;
    @Nonnull private final LocalDateTime creationDate;

    public TaskCreatedEvent(
            @Nonnull UUID publicId,
            @Nonnull UUID publicAssigneeId,
            @Nonnull String description,
            @Nonnull LocalDateTime creationDate
    ) {
        super(TASK_CREATED_EVENT_NAME, TASK_CREATED_EVENT_VERSION);

        this.publicId = publicId;
        this.publicAssigneeId = publicAssigneeId;
        this.description = description;
        this.creationDate = creationDate;
    }

    @Override
    public String recordKey() {
        return getPublicId().toString();
    }

    @Override
    public Object recordValue() {
        return this;
    }

}
