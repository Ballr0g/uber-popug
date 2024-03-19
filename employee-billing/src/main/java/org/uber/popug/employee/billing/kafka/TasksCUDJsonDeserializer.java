package org.uber.popug.employee.billing.kafka;

import org.uber.popug.employee.billing.kafka.event.cud.TaskCreatedReplicationEvent;

import java.util.Map;

public class TasksCUDJsonDeserializer extends HeaderBasedJsonDeserializer<Object> {

    private static final Map<String, Class<?>> EXPECTED_NAME_HEADER_VALUES = Map.ofEntries(
            Map.entry("task.entity.created", TaskCreatedReplicationEvent.class)
    );

    public TasksCUDJsonDeserializer() {
        super(EXPECTED_NAME_HEADER_VALUES);
    }

}
