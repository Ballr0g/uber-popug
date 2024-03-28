package org.uber.popug.schema.registry.exception;

import lombok.Getter;
import org.uber.popug.schema.registry.domain.schema.Schema;

@Getter
public class SchemaNotFoundException extends RuntimeException {

    private static final String SCHEMA_NOT_FOUND_MESSAGE_TEMPLATE
            = "Version %d of %s schema named %s does not exist.";

    private final Schema missingSchema;

    public SchemaNotFoundException(Schema missingSchema) {
        super(SCHEMA_NOT_FOUND_MESSAGE_TEMPLATE.formatted(
                missingSchema.version(),
                missingSchema.type().getTextType(),
                missingSchema.name()
        ));

        this.missingSchema = missingSchema;
    }

}
