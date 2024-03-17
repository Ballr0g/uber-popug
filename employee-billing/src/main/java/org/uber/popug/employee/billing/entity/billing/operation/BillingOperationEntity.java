package org.uber.popug.employee.billing.entity.billing.operation;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record BillingOperationEntity(
        long id,
        @Nonnull UUID publicId,
        long ownerUserId,
        @Nonnull String description,
        long credit,
        long debit,
        long billingCycleId
) {
}
