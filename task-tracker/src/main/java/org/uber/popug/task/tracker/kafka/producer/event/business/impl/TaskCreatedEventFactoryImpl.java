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
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCreatedEventV1;
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

    @Override
    public ProducerRecord<String, Object> createTaskCreatedEventV1(@Nonnull Task task, @Nonnull String topicName) {
        final var taskCreatedEventV1 = buildTaskCreatedEventFromBusiness(task);
        final var taskCreatedHeadersV1 = buildHeadersForEvent(taskCreatedEventV1);
        return asProducerRecord(taskCreatedEventV1, taskCreatedHeadersV1, topicName);
    }

    private TaskCreatedEventV1 buildTaskCreatedEventFromBusiness(Task task) {
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

    private Headers buildHeadersForEvent(TaskCreatedEventV1 taskCreatedEventV1) {
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

    private ProducerRecord<String, Object> asProducerRecord(
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
}
