package org.uber.popug.employee.billing.domain.aggregates;

import jakarta.annotation.Nonnull;
import org.uber.popug.employee.billing.domain.billing.PaymentData;
import org.uber.popug.employee.billing.domain.user.User;

public record PaymentDataWithUser(
        @Nonnull PaymentData data,
        @Nonnull User relatedUser
) {
}
