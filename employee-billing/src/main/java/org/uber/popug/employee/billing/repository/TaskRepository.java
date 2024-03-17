package org.uber.popug.employee.billing.repository;

import org.uber.popug.employee.billing.entity.task.TaskEntity;

public interface TaskRepository {

    long generateNextDbTaskId();

    int saveReplicated(TaskEntity task);

}
