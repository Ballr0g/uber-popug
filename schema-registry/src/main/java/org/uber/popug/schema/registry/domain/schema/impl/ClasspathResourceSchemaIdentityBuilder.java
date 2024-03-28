package org.uber.popug.schema.registry.domain.schema.impl;

import org.uber.popug.schema.registry.domain.schema.Schema;
import org.uber.popug.schema.registry.domain.schema.SchemaIdentityBuilder;

import java.util.regex.Pattern;

public class ClasspathResourceSchemaIdentityBuilder implements SchemaIdentityBuilder {

    private static final String SCHEMA_RESOURCE_PATH_TEMPLATE =
            "classpath:schemas/json/%s/%s/%d.json";

    private static final Pattern DASH_REGEX = Pattern.compile("-");

    @Override
    public String buildSchemaIdentity(Schema schema) {
        final var schemaNamePath = DASH_REGEX.matcher(schema.name()).replaceAll("/");
        return SCHEMA_RESOURCE_PATH_TEMPLATE.formatted(
                schema.type().getTextType(),
                schemaNamePath,
                schema.version()
        );
    }

}
