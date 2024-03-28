package org.uber.popug.employee.billing.kafka.event.cud;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;

public interface BillingOperationCreatedReplicationEventFactory {

    ProducerRecord<String, Object> createBillingOperationCreatedReplicationEvent(
            @Nonnull BillingOperationFullData billingOperation,
            @Nonnull String topicName
    );

}
