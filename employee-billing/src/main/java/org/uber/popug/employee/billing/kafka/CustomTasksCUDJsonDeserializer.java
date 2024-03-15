package org.uber.popug.employee.billing.kafka;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.Assert;
import org.uber.popug.employee.billing.kafka.event.cud.TaskCreatedReplicationEvent;

import java.io.IOException;
import java.util.Map;

public class CustomTasksCUDJsonDeserializer extends JsonDeserializer<Object> {
    private static final Map<String, Class<?>> EXPECTED_NAME_HEADER_VALUES = Map.ofEntries(
            Map.entry("task.entity.created", TaskCreatedReplicationEvent.class)
    );

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
        if (data == null) {
            return null;
        }

        Header typeHeader = headers.lastHeader("name");
        Assert.state(typeHeader != null, "No type information in headers and no default type provided");

        final var typeName = new String(typeHeader.value());
        final var targetType = EXPECTED_NAME_HEADER_VALUES.get(typeName);

        if (targetType == null) {
            throw new IllegalStateException("Unable to find target type for deserialization");
        }

        try {
            return objectMapper.readValue(data, targetType);
        } catch (IOException ioException) {
            throw new SerializationException("Can't deserialize data  from topic [" + topic + "]", ioException);
        }
    }
}
