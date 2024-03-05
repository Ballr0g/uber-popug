package org.uber.popug.task.tracker.entity.user;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.entity.userrole.UserRoleEntity;

import java.util.Set;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserEntity {
    @Nonnull private final UUID extUserId;
    @Nonnull private final String login;
    @Nonnull Set<UserRoleEntity> roles;
}
