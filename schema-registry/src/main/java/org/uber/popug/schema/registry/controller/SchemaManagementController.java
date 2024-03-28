package org.uber.popug.schema.registry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.uber.popug.schema.registry.service.SchemaRetrievalService;
import org.uber.popug.task.tracker.rest.generated.api.SchemaRetrievalApi;

@Controller
@RequiredArgsConstructor
public class SchemaManagementController implements SchemaRetrievalApi {

    private final SchemaRetrievalService schemaRetrievalService;

    @Override
    public ResponseEntity<String> getSchemaByNameAndVersion(
            String schemaType,
            String schemaName,
            Long schemaVersion
    ) {
        return ResponseEntity.ok(schemaRetrievalService.retrieveSchemaAsString(schemaType, schemaName, schemaVersion));
    }

}
