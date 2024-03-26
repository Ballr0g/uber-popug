package org.uber.popug.employee.billing.kafka.consumer;

import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.Optional;

public record NamedJsonSchema<T>(
        @Nonnull String name,
        @Nonnull Map<Integer, JsonSchemaWithClass<T>> supportedVersions
) {

    public Optional<JsonSchemaWithClass<T>> getSchemaWithClassByVersion(int version) {
        final var targetSchemaInfo = supportedVersions.get(version);
        return Optional.ofNullable(targetSchemaInfo);
    }

}
