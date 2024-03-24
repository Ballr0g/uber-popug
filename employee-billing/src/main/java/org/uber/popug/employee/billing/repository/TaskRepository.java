package org.uber.popug.employee.billing.repository;

import org.uber.popug.employee.billing.entity.composite.TaskToAssigneeEntity;
import org.uber.popug.employee.billing.entity.task.TaskEntity;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    long generateNextDbTaskId();

    int saveReplicated(TaskEntity task);

    Optional<TaskToAssigneeEntity> findTaskToAssigneeByPublicTaskId(UUID publicTaskId);

    Optional<TaskEntity> updateTaskAssigneeById(long taskId, long newAssigneeId);

    Optional<TaskEntity> completeTaskById(long taskId);

}
