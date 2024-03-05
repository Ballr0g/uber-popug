package org.uber.popug.task.tracker.repository;

import org.uber.popug.task.tracker.domain.task.Task;

public interface TaskRepository {

    int save(Task task);

}
