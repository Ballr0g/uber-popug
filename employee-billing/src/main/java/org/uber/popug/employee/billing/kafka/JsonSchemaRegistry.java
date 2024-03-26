package org.uber.popug.employee.billing.kafka;

import org.uber.popug.employee.billing.kafka.consumer.NamedJsonSchema;
import org.uber.popug.employee.billing.kafka.consumer.JsonSchemaWithClass;

import java.util.Optional;

public interface JsonSchemaRegistry<T> {

    Optional<NamedJsonSchema<T>> getIfSchemaSupported(String schemaName);

    default Optional<JsonSchemaWithClass<T>> getSchemaWithClassForVersion(String schemaName, int schemaVersion) {
        return getIfSchemaSupported(schemaName)
                .flatMap(schema -> schema.getSchemaWithClassByVersion(schemaVersion));
    }

}
