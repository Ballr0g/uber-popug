package org.uber.popug.schema.registry.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.uber.popug.task.tracker.rest.generated.api.SchemaRetrievalApi;
import org.uber.popug.task.tracker.rest.generated.model.SchemaEventTypeDto;

@Controller
public class SchemaManagementController implements SchemaRetrievalApi {

    @Override
    public ResponseEntity<String> getSchemaByNameAndVersion(
            SchemaEventTypeDto schemaType,
            String schemaName,
            Long schemaVersion
    ) {
        throw new UnsupportedOperationException(
                "SchemaManagementController.getSchemaByNameAndVersion is not implemented."
        );
    }

}
