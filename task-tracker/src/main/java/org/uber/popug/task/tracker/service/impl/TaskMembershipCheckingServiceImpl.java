package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletion;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletionPublic;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.repository.UserRepository;
import org.uber.popug.task.tracker.service.TaskMembershipCheckingService;

import java.util.Optional;

@RequiredArgsConstructor
public class TaskMembershipCheckingServiceImpl implements TaskMembershipCheckingService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<TaskForCompletion> checkCompletionValid(TaskForCompletionPublic task) {
        final var taskAssigneeEntityOptional = userRepository.findByPublicId(task.publicAssigneeId());
        final var taskForCompletionEntityOptional = taskRepository.findByPublicId(task.publicTaskId());

        if (taskAssigneeEntityOptional.isPresent() && taskForCompletionEntityOptional.isPresent()) {
            final var taskAssigneeEntity = taskAssigneeEntityOptional.get();
            final var taskForCompletionEntity = taskForCompletionEntityOptional.get();

            return taskAssigneeEntity.id() == taskForCompletionEntity.assigneeId()
                    ? Optional.of(new TaskForCompletion(
                    taskForCompletionEntity.id(),
                    taskAssigneeEntity.id()))
                    : Optional.empty();
        }

        return Optional.empty();
    }

}
