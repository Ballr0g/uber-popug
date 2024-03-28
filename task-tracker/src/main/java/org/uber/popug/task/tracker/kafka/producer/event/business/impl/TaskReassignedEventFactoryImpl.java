package org.uber.popug.task.tracker.kafka.producer.event.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.networknt.schema.JsonSchema;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.uber.popug.task.tracker.entity.composite.ReassignedTaskEntity;
import org.uber.popug.task.tracker.exception.technical.JsonSchemaEventViolationException;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskReassignedEventDataV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskReassignedEventDataV2;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskReassignedEventV1;
import org.uber.popug.task.tracker.kafka.generated.dto.TaskReassignedEventV2;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskReassignedEventFactory;
import org.uber.popug.task.tracker.utils.UberPopugServices;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
public class TaskReassignedEventFactoryImpl implements TaskReassignedEventFactory {

    private static final UberPopugServices EVENT_PRODUCER_SERVICE = UberPopugServices.TASK_TRACKER;

    private final ObjectMapper objectMapper;

    private final JsonSchema taskReassignedEventV1JsonSchema;
    private final JsonSchema taskReassignedEventV2JsonSchema;

    @Override
    public ProducerRecord<String, Object> createTaskReassignedEvent(ReassignedTaskEntity reassignedTask, String topicName) {
        if (reassignedTask.task().jiraId() == null) {
            return createTaskReassignedEventV1(reassignedTask, topicName);
        }
        return createTaskReassignedEventV2(reassignedTask, topicName);
    }

    private ProducerRecord<String, Object> createTaskReassignedEventV1(ReassignedTaskEntity reassignedTask, String topicName) {
        final var taskReassignedEventV1 = buildTaskReassignedEventV1FromBusiness(reassignedTask);
        final var taskReassignedHeadersV1 = buildHeadersForEventV1(taskReassignedEventV1);
        return asProducerRecordV1(taskReassignedEventV1, taskReassignedHeadersV1, topicName);
    }

    private TaskReassignedEventV1 buildTaskReassignedEventV1FromBusiness(ReassignedTaskEntity task) {
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskReassignedEventV1()
                .withEventName(TaskReassignedEventV1.EventName.TASK_REASSIGNED)
                .withEventVersion(TaskReassignedEventV1.EventVersion._1)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskReassignedEventDataV1()
                                .withTaskId(task.task().publicId())
                                .withPreviousAssigneeId(task.previousAssignee().extPublicId())
                                .withNewAssigneeId(task.newAssignee().extPublicId())
                                .withTaskDescription(task.task().description())
                                // Todo: get domain event reassignment time here.
                                .withReassignmentDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV1(TaskReassignedEventV1 taskReassignedEventV1) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", taskReassignedEventV1.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(taskReassignedEventV1.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", taskReassignedEventV1.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", taskReassignedEventV1.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV1(
            TaskReassignedEventV1 taskReassignedEvent,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskReassignedJson = objectMapper.valueToTree(taskReassignedEvent);
        final var validationResult = taskReassignedEventV1JsonSchema.validate(taskReassignedJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskReassignedEventV1JsonSchema);
        }

        final var taskReassignedProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskReassignedEvent.getData().getTaskId().toString(),
                taskReassignedEvent
        );

        producerHeaders.forEach(header -> taskReassignedProducerRecord.headers().add(header));
        return taskReassignedProducerRecord;
    }

    private ProducerRecord<String, Object> createTaskReassignedEventV2(ReassignedTaskEntity reassignedTask, String topicName) {
        final var taskReassignedEventV2 = buildTaskReassignedEventV2FromBusiness(reassignedTask);
        final var taskReassignedHeadersV2 = buildHeadersForEventV2(taskReassignedEventV2);
        return asProducerRecordV2(taskReassignedEventV2, taskReassignedHeadersV2, topicName);
    }

    private TaskReassignedEventV2 buildTaskReassignedEventV2FromBusiness(ReassignedTaskEntity task) {
        final var eventDate = LocalDateTime.now(ZoneOffset.UTC);

        return new TaskReassignedEventV2()
                .withEventName(TaskReassignedEventV2.EventName.TASK_REASSIGNED)
                .withEventVersion(TaskReassignedEventV2.EventVersion._2)
                .withEventId(UuidCreator.getTimeOrderedEpoch())
                .withEventTime(eventDate)
                .withProducer(EVENT_PRODUCER_SERVICE.getName())
                .withData(
                        new TaskReassignedEventDataV2()
                                .withTaskId(task.task().publicId())
                                .withPreviousAssigneeId(task.previousAssignee().extPublicId())
                                .withJiraId(task.task().jiraId())
                                .withNewAssigneeId(task.newAssignee().extPublicId())
                                .withTaskDescription(task.task().description())
                                // Todo: get domain event reassignment time here.
                                .withReassignmentDate(eventDate)
                );
    }

    private Headers buildHeadersForEventV2(TaskReassignedEventV2 taskReassignedEventV2) {
        return new RecordHeaders(
                List.of(
                        new RecordHeader("event-name", taskReassignedEventV2.getEventName().value().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-version", Integer.toString(taskReassignedEventV2.getEventVersion().value()).getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-id", taskReassignedEventV2.getEventId().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("event-time", taskReassignedEventV2.getEventTime().toString().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
                )
        );
    }

    private ProducerRecord<String, Object> asProducerRecordV2(
            TaskReassignedEventV2 taskReassignedEvent,
            Headers producerHeaders,
            String topicName
    ) {
        final var taskReassignedJson = objectMapper.valueToTree(taskReassignedEvent);
        final var validationResult = taskReassignedEventV2JsonSchema.validate(taskReassignedJson);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaEventViolationException(validationResult, taskReassignedEventV2JsonSchema);
        }

        final var taskReassignedProducerRecord = new ProducerRecord<String, Object>(
                topicName,
                taskReassignedEvent.getData().getTaskId().toString(),
                taskReassignedEvent
        );

        producerHeaders.forEach(header -> taskReassignedProducerRecord.headers().add(header));
        return taskReassignedProducerRecord;
    }

}
