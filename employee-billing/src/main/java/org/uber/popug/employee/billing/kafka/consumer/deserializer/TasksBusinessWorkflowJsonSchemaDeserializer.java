package org.uber.popug.employee.billing.kafka.consumer.deserializer;

import org.uber.popug.employee.billing.kafka.common.JsonSchemaRegistry;

public class TasksBusinessWorkflowJsonSchemaDeserializer extends HeaderBasedJsonSchemaDeserializer<Object> {

    public TasksBusinessWorkflowJsonSchemaDeserializer(JsonSchemaRegistry<Object> jsonSchemaRegistry) {
        super(jsonSchemaRegistry);
    }

}
