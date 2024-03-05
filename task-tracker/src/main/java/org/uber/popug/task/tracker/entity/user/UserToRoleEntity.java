package org.uber.popug.task.tracker.entity.user;

import jakarta.annotation.Nonnull;
import org.uber.popug.task.tracker.entity.userrole.UserRoleEntity;

import java.util.UUID;

public record UserToRoleEntity(
        long userId,
        @Nonnull UUID extPublicUserId,
        @Nonnull String userLogin,
        @Nonnull UserRoleEntity userRoleEntity
) {
}
