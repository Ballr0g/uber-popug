package org.uber.popug.task.tracker.entity.userrole;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRoleEntity {
    private final long userRoleId;
    @Nonnull private final String roleName;
}
