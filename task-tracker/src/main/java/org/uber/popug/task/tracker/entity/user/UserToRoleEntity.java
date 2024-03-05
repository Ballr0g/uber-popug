package org.uber.popug.task.tracker.entity.user;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.entity.userrole.UserRoleEntity;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserToRoleEntity {
    @Nonnull private final UUID userId;
    @Nonnull private final String userLogin;
    @Nonnull private final UserRoleEntity userRoleEntity;
}
