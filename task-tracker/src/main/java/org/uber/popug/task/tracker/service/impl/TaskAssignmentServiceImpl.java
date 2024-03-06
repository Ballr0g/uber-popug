package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.creation.TaskForCreation;
import org.uber.popug.task.tracker.domain.task.TaskIdProvider;
import org.uber.popug.task.tracker.mapping.UsersPersistenceMapper;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.RandomUserEntityService;
import org.uber.popug.task.tracker.service.TaskAssignmentService;

@RequiredArgsConstructor
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    private final UserRepository userRepository;
    private final RandomUserEntityService randomUserEntityService;
    private final UsersPersistenceMapper usersPersistenceMapper;
    private final TaskIdProvider taskIdProvider;
    @Override
    public Task assignNewTask(TaskForCreation task) {
        // Shuffling approaches:
        // 1) Materialized view over user ID.
        // 2) Cache + shuffle
        // 3) Random over DB
        final var availableTaskAssignees = userRepository.getDevelopers();
        final var selectedTaskAssigneeEntity = randomUserEntityService.getRandomUserEntityFromList(availableTaskAssignees);
        final var taskAssignee = usersPersistenceMapper.userEntityToTaskAssignee(selectedTaskAssigneeEntity);

        return Task.create(taskIdProvider, task.taskDescription(), taskAssignee);
    }
}
