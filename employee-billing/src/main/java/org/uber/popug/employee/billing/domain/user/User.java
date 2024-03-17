package org.uber.popug.employee.billing.domain.user;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record User(
        long id,
        @Nonnull UUID extPublicId,
        @Nonnull String login
) {
}
