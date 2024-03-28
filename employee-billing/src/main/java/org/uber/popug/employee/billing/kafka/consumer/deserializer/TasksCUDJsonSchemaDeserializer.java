package org.uber.popug.employee.billing.kafka.consumer.deserializer;

import org.uber.popug.employee.billing.kafka.common.JsonSchemaRegistry;

public class TasksCUDJsonSchemaDeserializer extends HeaderBasedJsonSchemaDeserializer<Object> {

    public TasksCUDJsonSchemaDeserializer(JsonSchemaRegistry<Object> jsonSchemaRegistry) {
        super(jsonSchemaRegistry);
    }

}
