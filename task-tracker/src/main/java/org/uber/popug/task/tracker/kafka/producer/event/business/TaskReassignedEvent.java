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

    @Nonnull private final UUID taskId;
    @Nonnull private final UUID previousAssigneeId;
    @Nonnull private final UUID newAssigneeId;
    @Nonnull private final String taskDescription;
    @Nonnull private final LocalDateTime reassignmentDate;

    public TaskReassignedEvent(
            @Nonnull UUID taskId,
            @Nonnull UUID previousAssigneeId,
            @Nonnull UUID newAssigneeId,
            @Nonnull String taskDescription,
            @Nonnull LocalDateTime reassignmentDate
    ) {
        super(TASK_REASSIGNED_EVENT_NAME, TASK_REASSIGNED_EVENT_VERSION);

        this.taskId = taskId;
        this.previousAssigneeId = previousAssigneeId;
        this.newAssigneeId = newAssigneeId;
        this.taskDescription = taskDescription;
        this.reassignmentDate = reassignmentDate;
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
