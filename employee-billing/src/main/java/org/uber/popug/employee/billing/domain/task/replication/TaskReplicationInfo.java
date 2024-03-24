package org.uber.popug.employee.billing.domain.task.replication;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskReplicationInfo(
        @Nonnull UUID id,
        @Nonnull UUID assigneeId,
        @Nonnull String description,
        @Nonnull LocalDateTime creationDate
) {
}
