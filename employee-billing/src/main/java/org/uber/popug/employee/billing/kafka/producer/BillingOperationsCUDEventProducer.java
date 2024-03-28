package org.uber.popug.employee.billing.kafka.producer;

import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;

public interface BillingOperationsCUDEventProducer {

    void sendTaskCreatedReplicationEvent(BillingOperationFullData billingOperation);

}
