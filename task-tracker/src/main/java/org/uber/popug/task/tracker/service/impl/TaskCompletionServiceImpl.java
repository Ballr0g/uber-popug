package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletion;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletionPublic;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.mapping.TasksKafkaEventMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.TaskCompletionService;
import org.uber.popug.task.tracker.service.TaskMembershipCheckingService;

@RequiredArgsConstructor
public class TaskCompletionServiceImpl implements TaskCompletionService {

    private final TaskMembershipCheckingService taskMembershipCheckingService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TasksKafkaEventMapper tasksKafkaEventMapper;
    private final TasksBusinessEventProducer tasksBusinessEventProducer;

    @Override
    public void completeTask(TaskForCompletionPublic task) {
        taskMembershipCheckingService.checkCompletionValid(task)
                .ifPresent(this::persistTaskCompletion);
    }

    private void persistTaskCompletion(TaskForCompletion task) {
        taskRepository.complete(task);

        final var taskEntity = taskRepository.findById(task.taskId());
        final var assigneeEntity = userRepository.findById(task.assigneeId());
        final var taskCompletedEvent = tasksKafkaEventMapper.toTaskCompletedEventFromEntities(
                taskEntity.get(), assigneeEntity.get()
        );

        tasksBusinessEventProducer.sendTaskCompletionEvent(taskCompletedEvent);
    }

}
