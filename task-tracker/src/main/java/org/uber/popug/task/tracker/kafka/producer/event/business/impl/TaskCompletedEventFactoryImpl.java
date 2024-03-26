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
import org.uber.popug.task.tracker.kafka.generated.dto.TaskCompletedEventV1;
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

    @Override
    public ProducerRecord<String, Object> createTaskCompletedEventV1(
            @Nonnull TaskEntity task,
            @Nonnull UserEntity assignee,
            @Nonnull String topicName
    ) {
        final var taskCompletedEventV1 = buildTaskCompletedEventFromBusiness(task, assignee);
        final var taskCompletedHeadersV1 = buildHeadersForEvent(taskCompletedEventV1);
        return asProducerRecord(taskCompletedEventV1, taskCompletedHeadersV1, topicName);
    }

    private TaskCompletedEventV1 buildTaskCompletedEventFromBusiness(TaskEntity task, UserEntity assignee) {
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

    private Headers buildHeadersForEvent(TaskCompletedEventV1 taskCreatedEventV1) {
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
}
