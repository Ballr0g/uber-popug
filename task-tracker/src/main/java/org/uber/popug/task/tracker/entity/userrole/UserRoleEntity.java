package org.uber.popug.task.tracker.entity.userrole;

import jakarta.annotation.Nonnull;

public record UserRoleEntity(
        long id,
        @Nonnull String name
) {
}
