package org.uber.popug.employee.billing.kafka;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

public abstract class HeaderBasedJsonDeserializer<T> extends JsonDeserializer<T> {

    private static final String DEFAULT_TYPE_HEADER_NAME = "name";

    protected final Map<String, Class<? extends T>> headerValueToTypeMap;
    protected final String typeHeaderName;

    protected HeaderBasedJsonDeserializer(
            Map<String, Class<? extends T>> headerValueToTypeMap
    ) {
        this(headerValueToTypeMap, DEFAULT_TYPE_HEADER_NAME);
    }

    protected HeaderBasedJsonDeserializer(
            Map<String, Class<? extends T>> headerValueToTypeMap,
            String typeHeaderName
    ) {
        this.headerValueToTypeMap = headerValueToTypeMap;
        this.typeHeaderName = typeHeaderName;
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        if (data == null) {
            return null;
        }

        Header typeHeader = headers.lastHeader(typeHeaderName);
        Assert.state(typeHeader != null, "No type information in headers and no default type provided");

        final var typeName = new String(typeHeader.value());
        final var targetType = headerValueToTypeMap.get(typeName);

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
