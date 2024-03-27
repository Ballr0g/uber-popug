package org.uber.popug.employee.billing.domain.task.replication;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskReplicationInfo(
        @Nonnull UUID id,
        @Nonnull UUID assigneeId,
        @Nullable String jiraId,
        @Nonnull String description,
        @Nonnull LocalDateTime creationDate
) {
}
