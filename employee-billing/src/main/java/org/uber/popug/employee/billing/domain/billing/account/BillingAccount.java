package org.uber.popug.employee.billing.domain.billing.account;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record BillingAccount(
        long id,
        @Nonnull UUID publicId,
        long currentTotal
) {
}
