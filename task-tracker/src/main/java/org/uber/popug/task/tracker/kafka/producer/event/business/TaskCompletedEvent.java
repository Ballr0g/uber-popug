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

    @Nonnull private final UUID taskId;
    @Nonnull private final UUID assigneeId;
    @Nonnull private final String taskDescription;
    @Nonnull private final LocalDateTime completionDate;

    public TaskCompletedEvent(
            @Nonnull UUID taskId,
            @Nonnull UUID assigneeId,
            @Nonnull String taskDescription,
            @Nonnull LocalDateTime completionDate
    ) {
        super(TASK_COMPLETED_EVENT_NAME, TASK_COMPLETED_EVENT_VERSION);

        this.taskId = taskId;
        this.assigneeId = assigneeId;
        this.taskDescription = taskDescription;
        this.completionDate = completionDate;
    }

    @Override
    public String recordKey() {
        return taskId.toString();
    }

    @Override
    public Object recordValue() {
        return this;
    }

}
