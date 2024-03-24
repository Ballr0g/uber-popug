package org.uber.popug.employee.billing.entity.composite;

import jakarta.annotation.Nonnull;
import org.uber.popug.employee.billing.entity.task.TaskEntity;
import org.uber.popug.employee.billing.entity.user.UserEntity;

public record TaskToAssigneeEntity(
        @Nonnull TaskEntity task,
        @Nonnull UserEntity taskAssignee
) {
}
