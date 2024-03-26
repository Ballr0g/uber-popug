package org.uber.popug.employee.billing.kafka;

public class TasksBusinessWorkflowJsonSchemaDeserializer extends HeaderBasedJsonSchemaDeserializer<Object> {

    public TasksBusinessWorkflowJsonSchemaDeserializer(JsonSchemaRegistry<Object> jsonSchemaRegistry) {
        super(jsonSchemaRegistry);
    }

}
