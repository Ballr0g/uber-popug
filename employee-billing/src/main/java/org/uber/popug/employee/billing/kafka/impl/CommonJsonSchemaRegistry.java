package org.uber.popug.employee.billing.kafka.impl;

import org.uber.popug.employee.billing.kafka.JsonSchemaRegistry;
import org.uber.popug.employee.billing.kafka.consumer.NamedJsonSchema;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommonJsonSchemaRegistry<T> implements JsonSchemaRegistry<T> {

    private final Map<String, NamedJsonSchema<T>> jsonSchemaInfoMap;

    public CommonJsonSchemaRegistry(Collection<NamedJsonSchema<T>> schemas) {
        // Todo: Handling of IllegalStateException on duplicate elements.
        this.jsonSchemaInfoMap = schemas.stream()
                .collect(Collectors.toMap(NamedJsonSchema::name, schema -> schema));
    }

    @Override
    public Optional<NamedJsonSchema<T>> getIfSchemaSupported(String schemaName) {
        if (schemaName == null) {
            return Optional.empty();
        }

        final var jsonSchemaInfo = jsonSchemaInfoMap.get(schemaName);
        return Optional.ofNullable(jsonSchemaInfo);
    }

}
