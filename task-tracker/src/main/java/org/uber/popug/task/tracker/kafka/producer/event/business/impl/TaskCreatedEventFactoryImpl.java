package org.uber.popug.task.tracker.kafka.producer.event.business.impl;

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
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedEventDataV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedEventDataV2;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedEventV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedEventV2;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCreatedEventFactory;
import org.uber.popug.task.tracker.utils.UberPopugServices;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
public class TaskCreatedEventFactoryImpl implements TaskCreatedEventFactory {

    private static final UberPopugServices EVENT_PRODUCER_SERVICE = UberPopugServices.TASK_TRACKER;

    private final ObjectMapper objectMapper;

    private final JsonSchema taskCreatedEventV1JsonSchema;
    private final JsonSchema taskCreatedEventV2JsonSchema;

    @Override
    public ProducerRecord<String, Object> createTaskCreatedEvent(@Nonnull Task task, @Nonnull String topicName) {
        if (task.jiraId() == null) {
            return createTaskCreatedEventV1(task, topicName);
        }
        return createTaskCreatedEventV2(task, topicName);
    }

    private ProducerRecord<String, Object> createTaskCreatedEventV1(@Nonnull Task task, @Nonnull String topicName) {
        final var taskCreatedEventV1 = buildTaskCreatedEventV1FromBusiness(task);
        final var taskCreatedHeadersV1 = buildHeadersForEventV1(taskCreatedEventV1);
        return asProducerRecordV1(taskCreatedEventV1, taskCreatedHeadersV1, topicName);
    }

    private TaskCreatedEventV1 buildTaskCreatedEventV1FromBusiness(Task task) {
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskCreatedEventV1()
                .withEventName(TaskCreatedEventV1.EventName.TASK_CREATED)
                .withEventVersion(TaskCreatedEventV1.EventVersion._1)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskCreatedEventDataV1()
                                .withTaskId(task.publicId())
                                .withAssigneeId(task.assignee().extPublicId())
                                .withTaskDescription(task.description())
                                // Todo: get domain event creation time here.
                                .withCreationDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV1(TaskCreatedEventV1 taskCreatedEventV1) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", taskCreatedEventV1.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(taskCreatedEventV1.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", taskCreatedEventV1.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", taskCreatedEventV1.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV1(
            TaskCreatedEventV1 taskCreatedEvent,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskCreatedJson = objectMapper.valueToTree(taskCreatedEvent);
        final var validationResult = taskCreatedEventV1JsonSchema.validate(taskCreatedJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskCreatedEventV1JsonSchema);
        }

        final var taskCreatedProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskCreatedEvent.getData().getTaskId().toString(),
                taskCreatedEvent
        );

        producerHeaders.forEach(header -> taskCreatedProducerRecord.headers().add(header));
        return taskCreatedProducerRecord;
    }

    private ProducerRecord<String, Object> createTaskCreatedEventV2(@Nonnull Task task, @Nonnull String topicName) {
        final var taskCreatedEventV2 = buildTaskCreatedEventV2FromBusiness(task);
        final var taskCreatedHeadersV2 = buildHeadersForEventV2(taskCreatedEventV2);
        return asProducerRecordV2(taskCreatedEventV2, taskCreatedHeadersV2, topicName);
    }

    private TaskCreatedEventV2 buildTaskCreatedEventV2FromBusiness(Task task) {
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskCreatedEventV2()
                .withEventName(TaskCreatedEventV2.EventName.TASK_CREATED)
                .withEventVersion(TaskCreatedEventV2.EventVersion._2)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskCreatedEventDataV2()
                                .withTaskId(task.publicId())
                                .withAssigneeId(task.assignee().extPublicId())
                                .withJiraId(task.jiraId())
                                .withTaskDescription(task.description())
                                // Todo: get domain event creation time here.
                                .withCreationDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV2(TaskCreatedEventV2 taskCreatedEventV2) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", taskCreatedEventV2.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(taskCreatedEventV2.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", taskCreatedEventV2.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", taskCreatedEventV2.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV2(
            TaskCreatedEventV2 taskCreatedEvent,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskCreatedJson = objectMapper.valueToTree(taskCreatedEvent);
        final var validationResult = taskCreatedEventV2JsonSchema.validate(taskCreatedJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskCreatedEventV2JsonSchema);
        }

        final var taskCreatedProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskCreatedEvent.getData().getTaskId().toString(),
                taskCreatedEvent
        );

        producerHeaders.forEach(header -> taskCreatedProducerRecord.headers().add(header));
        return taskCreatedProducerRecord;
    }
}
