package org.uber.popug.schema.registry.exception;

import lombok.Getter;

@Getter
public class InvalidSchemaRequestException extends RuntimeException {

    private static final String ILLEGAL_SCHEMA_ARGUMENTS_MESSAGE_TEMPLATE =
            "Impossible to create schema with type = %s, name = %s and version = %d.";

    private static final String UNKNOWN_SCHEMA_TYPE_MESSAGE_TEMPLATE =
            "Impossible to create schema with unknown type = %s.";

    private final String schemaType;
    private final String schemaName;
    private final Long schemaVersion;

    public InvalidSchemaRequestException(String schemaType, String schemaName, Long schemaVersion, Throwable cause) {
        super(
                ILLEGAL_SCHEMA_ARGUMENTS_MESSAGE_TEMPLATE.formatted(schemaType, schemaName, schemaVersion),
                cause
        );

        this.schemaType = schemaType;
        this.schemaName = schemaName;
        this.schemaVersion = schemaVersion;
    }

    public InvalidSchemaRequestException(String schemaType, String schemaName, Long schemaVersion) {
        super(ILLEGAL_SCHEMA_ARGUMENTS_MESSAGE_TEMPLATE.formatted(schemaType, schemaName, schemaVersion));

        this.schemaType = schemaType;
        this.schemaName = schemaName;
        this.schemaVersion = schemaVersion;
    }

}
