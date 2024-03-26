package org.uber.popug.employee.billing.kafka.consumer;

import com.networknt.schema.JsonSchema;
import jakarta.annotation.Nonnull;

public record JsonSchemaWithClass<T>(
        @Nonnull JsonSchema jsonSchema,
        @Nonnull Class<? extends T> schemaClass
) {
}
