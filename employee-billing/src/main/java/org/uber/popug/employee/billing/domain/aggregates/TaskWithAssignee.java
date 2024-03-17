package org.uber.popug.employee.billing.domain.aggregates;

import jakarta.annotation.Nonnull;
import org.uber.popug.employee.billing.domain.task.Task;
import org.uber.popug.employee.billing.domain.user.User;

public record TaskWithAssignee(
        @Nonnull Task task,
        @Nonnull User assignee
) {
}
