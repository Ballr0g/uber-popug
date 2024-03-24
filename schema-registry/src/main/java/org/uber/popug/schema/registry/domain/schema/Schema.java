package org.uber.popug.schema.registry.domain.schema;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.uber.popug.schema.registry.exception.InvalidSchemaRequestException;

import java.util.Objects;

public record Schema(
        @Nonnull Type type,
        @Nonnull String name,
        long version
) {

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        CUD("cud"),
        BUSINESS("business");

        private final String textType;
    }

    public static Schema fromRawValues(String schemaType, String schemaName, Long schemaVersion) {
        try {
            Objects.requireNonNull(schemaType);
            Objects.requireNonNull(schemaName);
            Objects.requireNonNull(schemaVersion);

            final var actualType = switch (schemaType.toLowerCase()) {
                case "cud" -> Type.CUD;
                case "business" -> Type.BUSINESS;
                default -> throw new InvalidSchemaRequestException(schemaType, schemaName, schemaVersion);
            };

            return new Schema(actualType, schemaName.toLowerCase(), schemaVersion);
        }
        catch (NullPointerException nullPointerException) {
            throw new InvalidSchemaRequestException(schemaType, schemaName, schemaVersion, nullPointerException);
        }
    }

}
