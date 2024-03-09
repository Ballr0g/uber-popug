package org.uber.popug.task.tracker.kafka.producer.event.business;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.uber.popug.task.tracker.kafka.producer.event.EventBase;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TaskReassignedEvent extends EventBase<String, Object> {

    private static final String TASK_REASSIGNED_EVENT_NAME = "task.reassigned";
    private static final String TASK_REASSIGNED_EVENT_VERSION = "1.0";

    @Nonnull private final UUID publicId;
    @Nonnull private final UUID previousAssigneePublicId;
    @Nonnull private final UUID newAssigneePublicId;
    @Nonnull private final String description;
    @Nonnull private final LocalDateTime reassignmentDate;

    public TaskReassignedEvent(
            @Nonnull UUID publicId,
            @Nonnull UUID previousAssigneePublicId,
            @Nonnull UUID newAssigneePublicId,
            @Nonnull String description,
            @Nonnull LocalDateTime reassignmentDate
    ) {
        super(TASK_REASSIGNED_EVENT_NAME, TASK_REASSIGNED_EVENT_VERSION);

        this.publicId = publicId;
        this.previousAssigneePublicId = previousAssigneePublicId;
        this.newAssigneePublicId = newAssigneePublicId;
        this.description = description;
        this.reassignmentDate = reassignmentDate;
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
