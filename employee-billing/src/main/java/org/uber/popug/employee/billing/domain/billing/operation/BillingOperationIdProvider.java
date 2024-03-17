package org.uber.popug.employee.billing.domain.billing.operation;

import java.util.UUID;

public interface BillingOperationIdProvider {

    UUID generatePublicBillingOperationId();

    long generateDbBillingOperationId();

}
