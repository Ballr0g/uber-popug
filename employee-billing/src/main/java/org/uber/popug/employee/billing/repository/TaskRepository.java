package org.uber.popug.employee.billing.repository;

import org.uber.popug.employee.billing.entity.task.TaskEntity;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    long generateNextDbTaskId();

    int saveReplicated(TaskEntity task);

    Optional<TaskEntity> findTaskByPublicId(UUID publicTaskId);

}
