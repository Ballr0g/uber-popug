package org.uber.popug.employee.billing.kafka;

import org.uber.popug.employee.billing.kafka.event.business.TaskCompletedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskReassignedEvent;

import java.util.Map;

public class TasksBusinessWorkflowJsonDeserializer extends HeaderBasedJsonDeserializer<Object> {
    private static final Map<String, Class<?>> EXPECTED_NAME_HEADER_VALUES = Map.ofEntries(
            Map.entry("task.created", TaskCreatedEvent.class),
            Map.entry("task.reassigned", TaskReassignedEvent.class),
            Map.entry("task.completed", TaskCompletedEvent.class)
    );

    public TasksBusinessWorkflowJsonDeserializer() {
        super(EXPECTED_NAME_HEADER_VALUES);
    }

}
