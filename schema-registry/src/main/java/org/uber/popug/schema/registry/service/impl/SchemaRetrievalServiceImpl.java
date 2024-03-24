package org.uber.popug.schema.registry.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.schema.registry.domain.schema.Schema;
import org.uber.popug.schema.registry.service.SchemaLoaderService;
import org.uber.popug.schema.registry.service.SchemaRetrievalService;

@RequiredArgsConstructor
public class SchemaRetrievalServiceImpl implements SchemaRetrievalService {

    private final SchemaLoaderService schemaLoaderService;

    @Override
    public String retrieveSchemaAsString(String schemaType, String schemaName, Long schemaVersion) {
        final var schema = Schema.fromRawValues(schemaType, schemaName, schemaVersion);
        return schemaLoaderService.retrieveSchemaContentsAsString(schema);
    }

}
