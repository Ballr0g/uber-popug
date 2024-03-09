package org.uber.popug.task.tracker.kafka.producer.event.business;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.uber.popug.task.tracker.kafka.producer.event.EventBase;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TaskCompletedEvent extends EventBase<String, Object> {

    private static final String TASK_COMPLETED_EVENT_NAME = "task.completed";
    private static final String TASK_COMPLETED_EVENT_VERSION = "1.0";

    @Nonnull private final UUID publicId;
    @Nonnull private final UUID extPublicAssigneeId;
    @Nonnull private final String description;
    @Nonnull private final LocalDateTime completionDate;

    public TaskCompletedEvent(
            @Nonnull UUID publicId,
            @Nonnull UUID extPublicAssigneeId,
            @Nonnull String description,
            @Nonnull LocalDateTime completionDate
    ) {
        super(TASK_COMPLETED_EVENT_NAME, TASK_COMPLETED_EVENT_VERSION);

        this.publicId = publicId;
        this.extPublicAssigneeId = extPublicAssigneeId;
        this.description = description;
        this.completionDate = completionDate;
    }

    @Override
    public String recordKey() {
        return publicId.toString();
    }

    @Override
    public Object recordValue() {
        return this;
    }

}
