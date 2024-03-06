package org.uber.popug.task.tracker.repository;

import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletion;
import org.uber.popug.task.tracker.entity.task.TaskEntity;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    long generateNextDbTaskId();

    int save(Task task);

    int complete(TaskForCompletion task);

    Optional<TaskEntity> findByPublicId(UUID publicTaskId);

}
