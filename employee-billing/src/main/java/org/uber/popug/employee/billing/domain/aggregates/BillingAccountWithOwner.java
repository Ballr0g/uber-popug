package org.uber.popug.employee.billing.domain.aggregates;

import jakarta.annotation.Nonnull;
import org.uber.popug.employee.billing.domain.billing.account.BillingAccount;
import org.uber.popug.employee.billing.domain.user.User;

public record BillingAccountWithOwner(
        @Nonnull BillingAccount account,
        @Nonnull User owner
) {
}
