package org.uber.popug.employee.billing.domain.aggregates;

import jakarta.annotation.Nonnull;
import org.uber.popug.employee.billing.domain.user.User;

public record TaskForReassignment(
        @Nonnull TaskWithAssignee taskWithCurrentAssignee,
        @Nonnull User newAssignee
) {
}
