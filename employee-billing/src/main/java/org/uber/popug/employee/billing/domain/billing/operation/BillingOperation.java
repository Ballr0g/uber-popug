package org.uber.popug.employee.billing.domain.billing.operation;

import jakarta.annotation.Nonnull;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.PaymentData;

import java.util.UUID;

public record BillingOperation(
        long id,
        @Nonnull UUID publicId,
        @Nonnull String description,
        @Nonnull PaymentData paymentData
) {

    public static BillingOperation forAssignment(
            BillingOperationIdProvider billingOperationIdProvider,
            BillingOperationDescriptionBuilder<TaskWithAssignee> billingOperationDescriptionBuilder,
            TaskWithAssignee taskWithAssignee
    ) {
        return new BillingOperation(
                billingOperationIdProvider.generateDbBillingOperationId(),
                billingOperationIdProvider.generatePublicBillingOperationId(),
                billingOperationDescriptionBuilder.buildBillingOperationDescription(taskWithAssignee),
                new PaymentData(
                        taskWithAssignee.task().costs().assignmentCost(),
                        0L
                )
        );
    }

    public static BillingOperation forCompletion(
            BillingOperationIdProvider billingOperationIdProvider,
            BillingOperationDescriptionBuilder<TaskWithAssignee> billingOperationDescriptionBuilder,
            TaskWithAssignee taskWithAssignee
    ) {
        return new BillingOperation(
                billingOperationIdProvider.generateDbBillingOperationId(),
                billingOperationIdProvider.generatePublicBillingOperationId(),
                billingOperationDescriptionBuilder.buildBillingOperationDescription(taskWithAssignee),
                new PaymentData(
                        0L,
                        taskWithAssignee.task().costs().completionCost()
                )
        );
    }

}
