package org.uber.popug.employee.billing.domain.billing.creation;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

// Todo: rename - org.uber.popug.employee.billing.domain.task.creation.TaskAssignmentInfo due to its general nature.
public record TaskForBillingAssignment(
        @Nonnull UUID publicId,
        @Nonnull UUID assigneeId,
        @Nonnull LocalDateTime creationDate
) {
}
