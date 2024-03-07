package org.uber.popug.task.tracker.repository;

import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletion;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    long generateNextDbTaskId();

    int save(Task task);

    int complete(TaskForCompletion task);

    Optional<TaskEntity> findById(long taskId);

    Optional<TaskEntity> findByPublicId(UUID publicTaskId);

    List<TaskEntity> readAllTasks();

    List<TaskEntity> findTasksByAssigneeId(long assigneeId);

    int reassignTask(TaskEntity task, UserEntity newAssignee);

}
