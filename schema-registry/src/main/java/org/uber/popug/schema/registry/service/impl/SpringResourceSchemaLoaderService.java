package org.uber.popug.schema.registry.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.uber.popug.schema.registry.domain.schema.Schema;
import org.uber.popug.schema.registry.domain.schema.SchemaIdentityBuilder;
import org.uber.popug.schema.registry.exception.SchemaNotFoundException;
import org.uber.popug.schema.registry.exception.technical.SchemaIOException;
import org.uber.popug.schema.registry.service.SchemaLoaderService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpringResourceSchemaLoaderService implements SchemaLoaderService {

    private final SchemaIdentityBuilder schemaIdentityBuilder;
    private final ResourceLoader resourceLoader;

    @Override
    public String retrieveSchemaContentsAsString(Schema schema) {
        final var schemaIdentityPath = schemaIdentityBuilder.buildSchemaIdentity(schema);

        final var targetResource = resourceLoader.getResource(schemaIdentityPath);
        if (!targetResource.exists()) {
            throw new SchemaNotFoundException(schema);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(targetResource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException ioException) {
            throw new SchemaIOException(schema, ioException);
        }
    }

}
