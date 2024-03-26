package org.uber.popug.employee.billing.kafka;

public class TasksCUDJsonSchemaDeserializer extends HeaderBasedJsonSchemaDeserializer<Object> {

    public TasksCUDJsonSchemaDeserializer(JsonSchemaRegistry<Object> jsonSchemaRegistry) {
        super(jsonSchemaRegistry);
    }

}
