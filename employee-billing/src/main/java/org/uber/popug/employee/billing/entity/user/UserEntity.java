package org.uber.popug.employee.billing.entity.user;

import jakarta.annotation.Nonnull;
import java.util.UUID;

public record UserEntity(
        long id,
        @Nonnull UUID extPublicId,
        @Nonnull String login
) {
}
