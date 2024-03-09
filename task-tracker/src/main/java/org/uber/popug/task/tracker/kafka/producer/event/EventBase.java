package org.uber.popug.task.tracker.kafka.producer.event;

import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.uber.popug.task.tracker.kafka.producer.KafkaProducerRecordEvent;
import org.uber.popug.task.tracker.utils.UberPopugServices;

import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class EventBase<K, V> implements KafkaProducerRecordEvent<K, V> {

    protected static final UberPopugServices EVENT_PRODUCER_SERVICE = UberPopugServices.TASK_TRACKER;

    protected final @Nonnull String eventName;
    protected final @Nonnull String eventVersion;

    private final List<RecordHeader> eventMetadata;

    protected EventBase(@Nonnull String eventName, @Nonnull String eventVersion) {
        this.eventName = eventName;
        this.eventVersion = eventVersion;
        this.eventMetadata = constructEventHeaders(eventName, eventVersion);
    }

    private static List<RecordHeader> constructEventHeaders(String eventName, String eventVersion) {
        return List.of(
                new RecordHeader("name", eventName.getBytes(StandardCharsets.UTF_8)),
                new RecordHeader("version", eventVersion.getBytes(StandardCharsets.UTF_8)),
                new RecordHeader("producer", EVENT_PRODUCER_SERVICE.getName().getBytes(StandardCharsets.UTF_8))
        );
    }

    public ProducerRecord<K, V> asProducerRecord(String topicName) {
        final var resultEventProducerRecord = new ProducerRecord<>(
                topicName,
                recordKey(),
                recordValue()
        );
        eventMetadata.forEach(recordHeader -> resultEventProducerRecord.headers().add(recordHeader));

        return resultEventProducerRecord;
    }
}
