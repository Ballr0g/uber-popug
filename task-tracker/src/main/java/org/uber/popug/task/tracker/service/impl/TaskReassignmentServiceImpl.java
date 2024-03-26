package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.entity.composite.ReassignedTaskEntity;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.RandomUserEntityService;
import org.uber.popug.task.tracker.service.TaskReassignmentService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TaskReassignmentServiceImpl implements TaskReassignmentService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RandomUserEntityService randomUserEntityService;
    private final TasksBusinessEventProducer tasksBusinessEventProducer;

    @Override
    public void reassignAllTasks() {
        final var allTasks = taskRepository.readAllTasks();
        final var allDevelopers = userRepository.getDevelopers();

        final var reassignedTasks = new ArrayList<ReassignedTaskEntity>();
        allTasks.forEach(task -> reassignedTasks.add(reassignTask(task, allDevelopers)));
        tasksBusinessEventProducer.sendTaskReassignmentEventsTransactional(reassignedTasks);
    }

    private ReassignedTaskEntity reassignTask(TaskEntity task, List<UserEntity> availableDevelopers) {
        final var newTaskAssignee = randomUserEntityService.getRandomUserEntityFromList(availableDevelopers);
        final var previousAssignee = userRepository.findById(task.assigneeId());

        taskRepository.reassignTask(task, newTaskAssignee);

        return new ReassignedTaskEntity(
                task,
                previousAssignee.get(),
                newTaskAssignee
        );
    }

}
