package org.uber.popug.task.tracker.kafka.producer.dto;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskCreatedEvent(
        @Nonnull UUID publicId,
        @Nonnull UUID publicAssigneeId,
        @Nonnull String description,
        @Nonnull LocalDateTime creationDate
) {
    private static final List<RecordHeader> TASK_CREATED_EVENT_TYPE = List.of(
            new RecordHeader("type", "task.created".getBytes(StandardCharsets.UTF_8)),
            new RecordHeader("version", "1.0".getBytes(StandardCharsets.UTF_8)),
            new RecordHeader("producer", "uber-popug.task-tracker".getBytes(StandardCharsets.UTF_8))
    );

    public ProducerRecord<String, Object> asProducerRecord(String topicName) {
        final var taskCreatedEventProducerRecord = new ProducerRecord<String, Object>(topicName, publicId().toString(), this);
        TASK_CREATED_EVENT_TYPE.forEach(recordHeader -> taskCreatedEventProducerRecord.headers().add(recordHeader));

        return taskCreatedEventProducerRecord;
    }
}
