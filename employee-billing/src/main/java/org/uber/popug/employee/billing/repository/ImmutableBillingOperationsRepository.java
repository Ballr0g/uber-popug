package org.uber.popug.employee.billing.repository;

import org.uber.popug.employee.billing.entity.billing.operation.BillingOperationEntity;

public interface ImmutableBillingOperationsRepository {

    long generateNextDbBillingOperationId();

    int appendBillingOperationEntry(BillingOperationEntity billingOperation);

}
