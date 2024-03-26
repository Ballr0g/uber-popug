package org.uber.popug.employee.billing.exception.technical;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JsonSchemaEventViolationException extends RuntimeException {

    private static final String FAILED_JSON_SCHEMA_VALIDATION_MESSAGE_TEMPLATE = "Error: validation of schema: %s"
            + System.lineSeparator() + "Reasons: %s";

    private final List<String> schemaViolations;
    private final String failedSchema;

    public JsonSchemaEventViolationException(Collection<ValidationMessage> schemaViolations, JsonSchema failedSchema) {
        super(FAILED_JSON_SCHEMA_VALIDATION_MESSAGE_TEMPLATE.formatted(
                failedSchema.toString(),
                schemaViolations.stream()
                        .map(ValidationMessage::toString)
                        .collect(Collectors.joining(System.lineSeparator()))
        ));

        this.schemaViolations = schemaViolations.stream().map(ValidationMessage::toString).toList();
        this.failedSchema = failedSchema.toString();
    }

}
