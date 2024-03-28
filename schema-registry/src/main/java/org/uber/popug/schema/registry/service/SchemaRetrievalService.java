package org.uber.popug.schema.registry.service;

public interface SchemaRetrievalService {

    String retrieveSchemaAsString(String schemaType, String schemaName, Long schemaVersion);

}
