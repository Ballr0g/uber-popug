package org.uber.popug.employee.billing.domain.task;

public interface TaskIdProvider {
    long generateDbTaskId();
}
