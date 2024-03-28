package org.uber.popug.task.tracker.kafka.producer.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.uber.popug.task.tracker.exception.technical.JsonSchemaEventViolationException;

// Todo: use this as target event producing solution.
public abstract class SchemaValidatedProducerRecordFactory<T, K, V> {

    protected final ObjectMapper objectMapper;
    protected final JsonSchema targetSchema;

    protected final String topicName;

    // Todo: protected final HeaderExtractor<T> headerExtractor;

    // Todo: protected final KeyExtractor<T> keyExtractor;

    protected SchemaValidatedProducerRecordFactory(
            ObjectMapper objectMapper,
            JsonSchema targetSchema,
            String topicName
    ) {
        this.objectMapper = objectMapper;
        this.targetSchema = targetSchema;
        this.topicName = topicName;
    }

    public final ProducerRecord<K, V> createProducerRecord(@Nonnull T eventSource) {
        // final var eventKey = keyExtractor.extract(eventSource);
        final var eventPayload = createEventPayload(eventSource);
        final var eventHeaders = buildHeadersForEvent(eventPayload);
        return asProducerRecord(eventPayload, /* eventKey, */ eventHeaders);
    }

    protected abstract V createEventPayload(T eventSource);

    private Headers buildHeadersForEvent(V eventPayload) {
        return new RecordHeaders() /* headerExtractor.extractHeaders(eventPayload) */;
    }

    private ProducerRecord<K, V> asProducerRecord(/* K eventKey, */ V eventPayload, Headers producerHeaders) {
        final var taskCreatedJson = objectMapper.valueToTree(eventPayload);
        final var validationResult = targetSchema.validate(taskCreatedJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, targetSchema);
        }

        final var taskCompletedProducerRecord = new ProducerRecord<K, V>(
                topicName,
                null /* eventKey */,
                eventPayload
        );

        producerHeaders.forEach(header -> taskCompletedProducerRecord.headers().add(header));
        return taskCompletedProducerRecord;
    }

}
