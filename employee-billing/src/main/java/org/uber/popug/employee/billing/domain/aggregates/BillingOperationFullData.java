package org.uber.popug.employee.billing.domain.aggregates;

import jakarta.annotation.Nonnull;
import org.uber.popug.employee.billing.domain.billing.cycle.BillingCycle;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperation;
import org.uber.popug.employee.billing.domain.user.User;

public record BillingOperationFullData(
        @Nonnull BillingOperation data,
        @Nonnull User ownerUser,
        @Nonnull BillingCycle ownerCycle
) {

    public static BillingOperationFullData assembleBillingOperationLogEntry(
            BillingOperation billingOperation,
            User ownerUser,
            BillingCycleProvider billingCycleProvider
    ) {
        return new BillingOperationFullData(
                billingOperation,
                ownerUser,
                billingCycleProvider.retrieveActiveBillingCycle()
        );
    }

}
