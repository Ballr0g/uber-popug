package org.uber.popug.employee.billing.domain.task.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.task.TaskIdProvider;
import org.uber.popug.employee.billing.repository.TaskRepository;

@RequiredArgsConstructor
public class PostgresTaskIdProvider implements TaskIdProvider {

    private final TaskRepository taskRepository;

    @Override
    public long generateDbTaskId() {
        return taskRepository.generateNextDbTaskId();
    }
}
