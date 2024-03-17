package org.uber.popug.employee.billing.kafka.event.cud;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCreatedReplicationEvent(
        @Nonnull UUID taskId,
        @Nonnull UUID assigneeId,
        @Nonnull String taskDescription,
        @Nonnull LocalDateTime creationDate
) {
}
