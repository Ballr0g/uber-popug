package org.uber.popug.employee.billing.domain.billing.operation;

@FunctionalInterface
public interface BillingOperationDescriptionBuilder<T> {

    String buildBillingOperationDescription(T operationDescriptionSource);

}
