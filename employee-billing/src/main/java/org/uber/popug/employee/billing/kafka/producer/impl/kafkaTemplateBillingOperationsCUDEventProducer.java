package org.uber.popug.employee.billing.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.kafka.event.cud.BillingOperationCreatedReplicationEventFactory;
import org.uber.popug.employee.billing.kafka.producer.BillingOperationsCUDEventProducer;

@RequiredArgsConstructor
public class kafkaTemplateBillingOperationsCUDEventProducer implements BillingOperationsCUDEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BillingOperationCreatedReplicationEventFactory billingOperationCreatedReplicationEventFactory;

    @Value("${kafka.producer.billing-operations-stream.topic}")
    private String billingOperationCUDKafkaTopicName;

    @Override
    public void sendTaskCreatedReplicationEvent(BillingOperationFullData billingOperation) {
        final var billingOperationCreatedProducerRecord = billingOperationCreatedReplicationEventFactory
                .createBillingOperationCreatedReplicationEvent(billingOperation, billingOperationCUDKafkaTopicName);

        kafkaTemplate.send(billingOperationCreatedProducerRecord);
    }
}
