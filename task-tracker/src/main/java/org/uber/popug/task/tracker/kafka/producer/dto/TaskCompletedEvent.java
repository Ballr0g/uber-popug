package org.uber.popug.task.tracker.kafka.producer.dto;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.uber.popug.task.tracker.kafka.producer.KafkaProducerRecordEvent;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskCompletedEvent(
        @Nonnull UUID publicId,
        @Nonnull UUID extPublicAssigneeId,
        @Nonnull String description,
        @Nonnull LocalDateTime completionDate
) implements KafkaProducerRecordEvent<String, Object> {

    private static final List<RecordHeader> TASK_COMPLETED_EVENT_TYPE = List.of(
            new RecordHeader("type", "task.completed".getBytes(StandardCharsets.UTF_8)),
            new RecordHeader("version", "1.0".getBytes(StandardCharsets.UTF_8)),
            new RecordHeader("producer", "uber-popug.task-tracker".getBytes(StandardCharsets.UTF_8))
    );

    @Override
    public ProducerRecord<String, Object> asProducerRecord(String topicName) {
        final var taskCompletedEventProducerRecord = new ProducerRecord<>(
                topicName,
                recordKey(),
                recordValue()
        );
        TASK_COMPLETED_EVENT_TYPE.forEach(recordHeader -> taskCompletedEventProducerRecord.headers().add(recordHeader));

        return taskCompletedEventProducerRecord;
    }

    @Override
    public String recordKey() {
        return publicId.toString();
    }

    @Override
    public Object recordValue() {
        return this;
    }
}
