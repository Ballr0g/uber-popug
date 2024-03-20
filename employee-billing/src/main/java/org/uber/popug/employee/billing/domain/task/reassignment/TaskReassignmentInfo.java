package org.uber.popug.employee.billing.domain.task.reassignment;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskReassignmentInfo(
        @Nonnull UUID taskExtPublicId,
        @Nonnull UUID previousAssigneeExtPublicId,
        @Nonnull UUID newAssigneeExtPublicId,
        @Nonnull String taskDescription,
        @Nonnull LocalDateTime reassignmentDate
) {
}
