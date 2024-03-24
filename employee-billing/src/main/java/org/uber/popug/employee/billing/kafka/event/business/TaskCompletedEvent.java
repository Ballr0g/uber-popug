package org.uber.popug.employee.billing.kafka.event.business;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCompletedEvent(
        @Nonnull UUID taskId,
        @Nonnull UUID assigneeId,
        @Nonnull String taskDescription,
        @Nonnull LocalDateTime completionDate
) {
}
