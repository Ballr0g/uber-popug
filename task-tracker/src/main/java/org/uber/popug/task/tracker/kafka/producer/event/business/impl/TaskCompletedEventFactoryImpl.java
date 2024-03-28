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
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.exception.technical.JsonSchemaEventViolationException;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCompletedEventDataV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCompletedEventDataV2;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCompletedEventV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCompletedEventV2;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCompletedEventFactory;
import org.uber.popug.task.tracker.utils.UberPopugServices;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
public class TaskCompletedEventFactoryImpl implements TaskCompletedEventFactory {

    private static final UberPopugServices EVENT_PRODUCER_SERVICE = UberPopugServices.TASK_TRACKER;

    private final ObjectMapper objectMapper;

    private final JsonSchema taskCompletedEventV1JsonSchema;
    private final JsonSchema taskCompletedEventV2JsonSchema;

    @Override
    public ProducerRecord<String, Object> createTaskCompletedEvent(
            @Nonnull TaskEntity task,
            @Nonnull UserEntity assignee,
            @Nonnull String topicName
    ) {
        if (task.jiraId() == null) {
            return createTaskCompletedEventV1(task, assignee, topicName);
        }
        return createTaskCompletedEventV2(task, assignee, topicName);
    }

    private ProducerRecord<String, Object> createTaskCompletedEventV1(
            @Nonnull TaskEntity task,
            @Nonnull UserEntity assignee,
            @Nonnull String topicName
    ) {
        final var taskCompletedEventV1 = buildTaskCompletedV1EventFromBusiness(task, assignee);
        final var taskCompletedHeadersV1 = buildHeadersForEventV1(taskCompletedEventV1);
        return asProducerRecordV1(taskCompletedEventV1, taskCompletedHeadersV1, topicName);
    }

    private TaskCompletedEventV1 buildTaskCompletedV1EventFromBusiness(TaskEntity task, UserEntity assignee) {
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskCompletedEventV1()
                .withEventName(TaskCompletedEventV1.EventName.TASK_COMPLETED)
                .withEventVersion(TaskCompletedEventV1.EventVersion._1)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskCompletedEventDataV1()
                                .withTaskId(task.publicId())
                                .withAssigneeId(assignee.extPublicId())
                                .withTaskDescription(task.description())
                                // Todo: get domain event completion time here.
                                .withCompletionDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV1(TaskCompletedEventV1 taskCreatedEventV1) {
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
            TaskCompletedEventV1 taskCompletedEventV1,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskCreatedJson = objectMapper.valueToTree(taskCompletedEventV1);
        final var validationResult = taskCompletedEventV1JsonSchema.validate(taskCreatedJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskCompletedEventV1JsonSchema);
        }

        final var taskCompletedProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskCompletedEventV1.getData().getTaskId().toString(),
                taskCompletedEventV1
        );

        producerHeaders.forEach(header -> taskCompletedProducerRecord.headers().add(header));
        return taskCompletedProducerRecord;
    }

    private ProducerRecord<String, Object> createTaskCompletedEventV2(
            @Nonnull TaskEntity task,
            @Nonnull UserEntity assignee,
            @Nonnull String topicName
    ) {
        final var taskCompletedEventV2 = buildTaskCompletedV2EventFromBusiness(task, assignee);
        final var taskCompletedHeadersV2 = buildHeadersForEventV2(taskCompletedEventV2);
        return asProducerRecordV2(taskCompletedEventV2, taskCompletedHeadersV2, topicName);
    }

    private TaskCompletedEventV2 buildTaskCompletedV2EventFromBusiness(TaskEntity task, UserEntity assignee) {
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskCompletedEventV2()
                .withEventName(TaskCompletedEventV2.EventName.TASK_COMPLETED)
                .withEventVersion(TaskCompletedEventV2.EventVersion._2)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskCompletedEventDataV2()
                                .withTaskId(task.publicId())
                                .withAssigneeId(assignee.extPublicId())
                                .withJiraId(task.jiraId())
                                .withTaskDescription(task.description())
                                // Todo: get domain event completion time here.
                                .withCompletionDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV2(TaskCompletedEventV2 taskCompletedEventV2) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", taskCompletedEventV2.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(taskCompletedEventV2.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", taskCompletedEventV2.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", taskCompletedEventV2.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV2(
            TaskCompletedEventV2 taskCompletedEventV2,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskCreatedJson = objectMapper.valueToTree(taskCompletedEventV2);
        final var validationResult = taskCompletedEventV2JsonSchema.validate(taskCreatedJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskCompletedEventV2JsonSchema);
        }

        final var taskCompletedProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskCompletedEventV2.getData().getTaskId().toString(),
                taskCompletedEventV2
        );

        producerHeaders.forEach(header -> taskCompletedProducerRecord.headers().add(header));
        return taskCompletedProducerRecord;
    }

}
