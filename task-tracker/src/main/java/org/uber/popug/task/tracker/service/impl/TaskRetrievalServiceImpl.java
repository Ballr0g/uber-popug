package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.exception.UserNotFoundException;
import org.uber.popug.task.tracker.mapping.TasksPersistenceMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.TaskRetrievalService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TaskRetrievalServiceImpl implements TaskRetrievalService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TasksPersistenceMapper tasksPersistenceMapper;

    @Override
    public List<Task> getTasksForPublicAssigneeId(UUID publicAssigneeId) {
        final var assigneeOptional = userRepository.findByPublicId(publicAssigneeId);

        if (assigneeOptional.isEmpty()) {
            throw UserNotFoundException.forPublicUserId(publicAssigneeId);
        }

        final var assignee = assigneeOptional.get();
        final var tasksForUser = taskRepository.findTasksByAssigneeId(assignee.id());

        return tasksForUser.stream()
                .map(task -> tasksPersistenceMapper.taskEntityToBusiness(task, assignee))
                .toList();
    }

}
