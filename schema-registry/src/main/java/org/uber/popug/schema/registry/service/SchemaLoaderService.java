package org.uber.popug.schema.registry.service;

import org.uber.popug.schema.registry.domain.schema.Schema;

public interface SchemaLoaderService {

    String retrieveSchemaContentsAsString(Schema schema);

}
