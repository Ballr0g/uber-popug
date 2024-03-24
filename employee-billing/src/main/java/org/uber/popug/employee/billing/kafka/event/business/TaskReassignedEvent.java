package org.uber.popug.employee.billing.kafka.event.business;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskReassignedEvent(
        @Nonnull UUID taskId,
        @Nonnull UUID previousAssigneeId,
        @Nonnull UUID newAssigneeId,
        @Nonnull String taskDescription,
        @Nonnull LocalDateTime reassignmentDate
) {
}
