package org.uber.popug.employee.billing.entity.billing.account;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record BillingAccountEntity(
        long id,
        @Nonnull UUID publicId,
        long ownerUserId,
        long currentTotal
) {
}
