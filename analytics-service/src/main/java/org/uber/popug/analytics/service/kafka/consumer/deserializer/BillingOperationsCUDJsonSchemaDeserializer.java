package org.uber.popug.analytics.service.kafka.consumer.deserializer;

import org.uber.popug.analytics.service.kafka.common.JsonSchemaRegistry;

public class BillingOperationsCUDJsonSchemaDeserializer extends HeaderBasedJsonSchemaDeserializer<Object> {

    public BillingOperationsCUDJsonSchemaDeserializer(JsonSchemaRegistry<Object> jsonSchemaRegistry) {
        super(jsonSchemaRegistry);
    }

}
