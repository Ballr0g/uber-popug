package org.uber.popug.task.tracker.kafka.producer.event.cud.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.networknt.schema.JsonSchema;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.exception.technical.JsonSchemaEventViolationException;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedReplicationEventDataV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedReplicationEventDataV2;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedReplicationEventV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedReplicationEventV2;
import org.uber.popug.task.tracker.kafka.producer.event.cud.TaskCreatedReplicationEventFactory;
import org.uber.popug.task.tracker.utils.UberPopugServices;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
public class TaskCreatedReplicationEventFactoryImpl implements TaskCreatedReplicationEventFactory {

    private static final UberPopugServices EVENT_PRODUCER_SERVICE = UberPopugServices.TASK_TRACKER;

    private final ObjectMapper objectMapper;

    private final JsonSchema taskCreatedReplicationEventV1JsonSchema;
    private final JsonSchema taskCreatedReplicationEventV2JsonSchema;

    @Override
    public ProducerRecord<String, Object> createReplicationEvent(@Nonnull Task task, @Nonnull String topicName) {
        if (task.jiraId() == null) {
            return createReplicationEventV1(task, topicName);
        }
        return createReplicationEventV2(task, topicName);
    }

    private ProducerRecord<String, Object> createReplicationEventV1(@Nonnull Task task, @Nonnull String topicName) {
        final var taskCreatedReplicationEvent = buildTaskReplicationEventV1FromBusiness(task);
        final var taskCreatedHeaders = buildHeadersForEventV1(taskCreatedReplicationEvent);

        return asProducerRecordV1(taskCreatedReplicationEvent, taskCreatedHeaders, topicName);
    }

    private TaskCreatedReplicationEventV1 buildTaskReplicationEventV1FromBusiness(Task task) {
        // Todo: integrate eventDate into the domain model.
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskCreatedReplicationEventV1()
                .withEventName(TaskCreatedReplicationEventV1.EventName.TASK_ENTITY_CREATED)
                .withEventVersion(TaskCreatedReplicationEventV1.EventVersion._1)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskCreatedReplicationEventDataV1()
                                .withTaskId(task.publicId())
                                .withAssigneeId(task.assignee().extPublicId())
                                .withTaskDescription(task.description())
                                // Todo: get domain event creation time here.
                                .withCreationDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV1(TaskCreatedReplicationEventV1 taskCreatedReplicationEventV1) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", taskCreatedReplicationEventV1.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(taskCreatedReplicationEventV1.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", taskCreatedReplicationEventV1.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", taskCreatedReplicationEventV1.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV1(
            TaskCreatedReplicationEventV1 taskCreatedEvent,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskCreatedReplicationJson = objectMapper.valueToTree(taskCreatedEvent);
        final var validationResult = taskCreatedReplicationEventV1JsonSchema.validate(taskCreatedReplicationJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskCreatedReplicationEventV1JsonSchema);
        }

        final var taskCreatedReplicationProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskCreatedEvent.getData().getTaskId().toString(),
                taskCreatedEvent
        );

        producerHeaders.forEach(header -> taskCreatedReplicationProducerRecord.headers().add(header));
        return taskCreatedReplicationProducerRecord;
    }

    private ProducerRecord<String, Object> createReplicationEventV2(@Nonnull Task task, @Nonnull String topicName) {
        final var taskCreatedReplicationEvent = buildTaskReplicationEventV2FromBusiness(task);
        final var taskCreatedHeaders = buildHeadersForEventV2(taskCreatedReplicationEvent);

        return asProducerRecordV2(taskCreatedReplicationEvent, taskCreatedHeaders, topicName);
    }

    private TaskCreatedReplicationEventV2 buildTaskReplicationEventV2FromBusiness(Task task) {
        // Todo: integrate eventDate into the domain model.
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskCreatedReplicationEventV2()
                .withEventName(TaskCreatedReplicationEventV2.EventName.TASK_ENTITY_CREATED)
                .withEventVersion(TaskCreatedReplicationEventV2.EventVersion._2)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskCreatedReplicationEventDataV2()
                                .withTaskId(task.publicId())
                                .withAssigneeId(task.assignee().extPublicId())
                                .withJiraId(task.jiraId())
                                .withTaskDescription(task.description())
                                // Todo: get domain event creation time here.
                                .withCreationDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV2(TaskCreatedReplicationEventV2 taskCreatedReplicationEventV2) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", taskCreatedReplicationEventV2.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(taskCreatedReplicationEventV2.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", taskCreatedReplicationEventV2.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", taskCreatedReplicationEventV2.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV2(
            TaskCreatedReplicationEventV2 taskCreatedEvent,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskCreatedReplicationJson = objectMapper.valueToTree(taskCreatedEvent);
        final var validationResult = taskCreatedReplicationEventV2JsonSchema.validate(taskCreatedReplicationJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskCreatedReplicationEventV2JsonSchema);
        }

        final var taskCreatedReplicationProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskCreatedEvent.getData().getTaskId().toString(),
                taskCreatedEvent
        );

        producerHeaders.forEach(header -> taskCreatedReplicationProducerRecord.headers().add(header));
        return taskCreatedReplicationProducerRecord;
    }

}
