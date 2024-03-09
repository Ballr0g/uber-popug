package org.uber.popug.task.tracker.entity.user;

import jakarta.annotation.Nonnull;
import org.uber.popug.task.tracker.entity.userrole.UserRoleEntity;

import java.util.Set;
import java.util.UUID;

public record UserEntity(
        long id,
        @Nonnull UUID extPublicId,
        @Nonnull String login,
        @Nonnull Set<UserRoleEntity> roles
) {
}
