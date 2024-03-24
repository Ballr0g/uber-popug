package org.uber.popug.schema.registry.exception.technical;

import lombok.Getter;
import org.uber.popug.schema.registry.domain.schema.Schema;

import java.io.IOException;

@Getter
public class SchemaIOException extends RuntimeException {

    private static final String SCHEMA_IO_EXCEPTION_MESSAGE_TEMPLATE =
            "Failure on loading schema %s. Cause: %s.";

    private final Schema failedSchema;

    public SchemaIOException(Schema failedSchema, IOException cause) {
        super(SCHEMA_IO_EXCEPTION_MESSAGE_TEMPLATE.formatted(
                failedSchema.toString(),
                cause.getMessage()
                ),
                cause
        );

        this.failedSchema = failedSchema;
    }

}
