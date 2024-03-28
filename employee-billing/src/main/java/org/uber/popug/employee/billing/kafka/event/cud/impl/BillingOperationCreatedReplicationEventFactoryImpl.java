package org.uber.popug.employee.billing.kafka.event.cud.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.networknt.schema.JsonSchema;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.exception.technical.JsonSchemaEventViolationException;
import org.uber.popug.employee.billing.kafka.event.cud.BillingOperationCreatedReplicationEventFactory;
import org.uber.popug.employee.billing.kafka.generated.dto.BillingOperationCreatedEventDataV1;
import org.uber.popug.employee.billing.kafka.generated.dto.BillingOperationCreatedEventV1;
import org.uber.popug.employee.billing.utils.UberPopugServices;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
public class BillingOperationCreatedReplicationEventFactoryImpl implements BillingOperationCreatedReplicationEventFactory {

    private static final UberPopugServices EVENT_PRODUCER_SERVICE = UberPopugServices.EMPLOYEE_BILLING;

    private final ObjectMapper objectMapper;

    private final JsonSchema billingOperationCreatedReplicationEventV1JsonSchema;

    @Override
    public ProducerRecord<String, Object> createBillingOperationCreatedReplicationEvent(
            BillingOperationFullData billingOperation,
            String topicName
    ) {
        final var billingOperationCreatedReplicationEvent = buildBillingOperationCreatedReplicationEventV1FromBusiness(billingOperation);
        final var taskCreatedHeaders = buildHeadersForEventV1(billingOperationCreatedReplicationEvent);

        return asProducerRecordV1(billingOperationCreatedReplicationEvent, taskCreatedHeaders, topicName);
    }

    private BillingOperationCreatedEventV1 buildBillingOperationCreatedReplicationEventV1FromBusiness(
            BillingOperationFullData billingOperation
    ) {
        // Todo: integrate eventDate into the domain model.
        final var paymentData = billingOperation.data().paymentData();
        final var operationType = paymentData.credit() > 0
                ? BillingOperationCreatedEventDataV1.OperationType.CREDIT
                : BillingOperationCreatedEventDataV1.OperationType.DEBIT;
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new BillingOperationCreatedEventV1()
                .withEventName(BillingOperationCreatedEventV1.EventName.BILLING_OPERATION_ENTITY_CREATED)
                .withEventVersion(BillingOperationCreatedEventV1.EventVersion._1)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new BillingOperationCreatedEventDataV1()
                                .withBillingOperationId(billingOperation.data().publicId())
                                .withAssociatedUserId(billingOperation.ownerUser().extPublicId())
                                .withBilledFor("task")
                                .withBillingOperationDescription(billingOperation.data().description())
                                .withOperationType(operationType)
                                .withOperationTotal(
                                        operationType == BillingOperationCreatedEventDataV1.OperationType.CREDIT
                                                ? paymentData.credit()
                                                : paymentData.debit()
                                )
                                // Todo: get domain event creation time here.
                                .withOperationDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV1(BillingOperationCreatedEventV1 billingOperationCreatedReplicationEventV1) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", billingOperationCreatedReplicationEventV1.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(billingOperationCreatedReplicationEventV1.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", billingOperationCreatedReplicationEventV1.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", billingOperationCreatedReplicationEventV1.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV1(
            BillingOperationCreatedEventV1 billingOperationCreatedEventV1,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskCreatedReplicationJson = objectMapper.valueToTree(billingOperationCreatedEventV1);
        final var validationResult = billingOperationCreatedReplicationEventV1JsonSchema.validate(taskCreatedReplicationJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, billingOperationCreatedReplicationEventV1JsonSchema);
        }

        final var taskCreatedReplicationProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                billingOperationCreatedEventV1.getData().getBillingOperationId().toString(),
                billingOperationCreatedEventV1
        );

        producerHeaders.forEach(header -> taskCreatedReplicationProducerRecord.headers().add(header));
        return taskCreatedReplicationProducerRecord;
    }
}
