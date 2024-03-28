package org.uber.popug.task.tracker.entity.composite;

import jakarta.annotation.Nonnull;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;

public record ReassignedTaskEntity(
        @Nonnull TaskEntity task,
        @Nonnull UserEntity previousAssignee,
        @Nonnull UserEntity newAssignee
) {
}
