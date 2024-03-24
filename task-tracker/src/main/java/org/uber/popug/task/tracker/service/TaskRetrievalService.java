package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.domain.task.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRetrievalService {

    List<Task> getTasksForPublicAssigneeId(UUID publicAssigneeId);

}
