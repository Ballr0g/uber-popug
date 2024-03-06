package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.RandomUserEntityService;
import org.uber.popug.task.tracker.service.TaskReassignmentService;

import java.util.List;

@RequiredArgsConstructor
public class TaskReassignmentServiceImpl implements TaskReassignmentService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RandomUserEntityService randomUserEntityService;

    @Override
    public void reassignAllTasks() {
        final var allTasks = taskRepository.readAllTasks();
        final var allDevelopers = userRepository.getDevelopers();

        allTasks.forEach(task -> reassignTask(task, allDevelopers));
    }

    private void reassignTask(TaskEntity task, List<UserEntity> availableDevelopers) {
        final var newTaskAssignee = randomUserEntityService.getRandomUserEntityFromList(availableDevelopers);

        taskRepository.reassignTask(task, newTaskAssignee);
    }

}
