package org.uber.popug.employee.billing.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.Assert;
import org.uber.popug.employee.billing.exception.technical.JsonClassMappingImpossibleException;
import org.uber.popug.employee.billing.exception.technical.JsonSchemaEventViolationException;
import org.uber.popug.employee.billing.kafka.consumer.NamedJsonSchema;
import org.uber.popug.employee.billing.kafka.consumer.JsonSchemaWithClass;

import java.io.IOException;

public abstract class HeaderBasedJsonSchemaDeserializer<T> extends JsonDeserializer<T> {

    private static final String DEFAULT_TYPE_HEADER_NAME = "event-name";
    private static final String DEFAULT_VERSION_HEADER_NAME = "event-version";

    protected final JsonSchemaRegistry<T> jsonSchemaRegistry;
    protected final String eventNameHeader;
    protected final String eventVersionHeader;

    protected HeaderBasedJsonSchemaDeserializer(JsonSchemaRegistry<T> jsonSchemaRegistry) {
        this(jsonSchemaRegistry, DEFAULT_TYPE_HEADER_NAME, DEFAULT_VERSION_HEADER_NAME);
    }

    protected HeaderBasedJsonSchemaDeserializer(
            JsonSchemaRegistry<T> jsonSchemaRegistry,
            String eventNameHeader,
            String eventVersionHeader
    ) {
        this.jsonSchemaRegistry = jsonSchemaRegistry;
        this.eventNameHeader = eventNameHeader;
        this.eventVersionHeader = eventVersionHeader;
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        if (data == null) {
            return null;
        }

        final var schemaInfo = retrieveSchemaInfoIfEventNameHeaderPresent(headers);
        final var targetSchemaWithClass = retrieveSchemaClassIfVersionAvailable(schemaInfo, headers);

        return deserializeWithJsonSchema(targetSchemaWithClass, data, topic);
    }

    private NamedJsonSchema<T> retrieveSchemaInfoIfEventNameHeaderPresent(Headers headers) {
        Header typeHeader = headers.lastHeader(eventNameHeader);
        Assert.state(typeHeader != null, "No type information in headers and no default type provided");

        final var eventName = new String(typeHeader.value());
        final var schemaInfo = jsonSchemaRegistry.getIfSchemaSupported(eventName);

        return schemaInfo.orElseThrow(() -> {
            throw new IllegalStateException("Unable to find target type for deserialization.");
        });
    }

    private JsonSchemaWithClass<? extends T> retrieveSchemaClassIfVersionAvailable(
            NamedJsonSchema<T> namedJsonSchema,
            Headers headers
    ) {
        Header typeHeader = headers.lastHeader(eventVersionHeader);
        Assert.state(eventVersionHeader != null, "No version information in headers and no default type provided");

        try {
            final var headerVersionString = new String(typeHeader.value());
            final var supportedVersion = namedJsonSchema.getSchemaWithClassByVersion(
                    Integer.parseInt(headerVersionString)
            );

            return supportedVersion.orElseThrow(() -> {
                throw new IllegalArgumentException("Unsupported version requested: %s".formatted(headerVersionString));
            });
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException("Unexpected version header value provided.", numberFormatException);
        }
    }

    private T deserializeWithJsonSchema(
            JsonSchemaWithClass<? extends T> jsonSchemaWithClass,
            byte[] data,
            String topic
    ) {
        try {
            final var jsonModel = objectMapper.readTree(data);
            final var targetSchema = jsonSchemaWithClass.jsonSchema();
            final var targetClass = jsonSchemaWithClass.schemaClass();

            final var schemaValidationErrors = targetSchema.validate(jsonModel);
            if (!schemaValidationErrors.isEmpty()) {
                throw new JsonSchemaEventViolationException(schemaValidationErrors, targetSchema);
            }

            return convertRawJsonToTargetClass(jsonModel, targetClass);
        }
        catch (IOException ioException) {
            throw new SerializationException("Can't deserialize data  from topic [" + topic + "]", ioException);
        }
    }

    private T convertRawJsonToTargetClass(TreeNode rawJsonTreeRoot, Class<? extends T> targetClass) {
        try {
            return objectMapper.treeToValue(rawJsonTreeRoot, targetClass);
        }
        catch (JsonProcessingException | IllegalArgumentException typeMappingException) {
            // Both exceptions may occur for ObjectMapper.treeToValue, meaning the type mapping provided is invalid.
            throw new JsonClassMappingImpossibleException(targetClass, rawJsonTreeRoot, typeMappingException);
        }
    }

}
