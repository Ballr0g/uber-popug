package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletionPublic;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.service.TaskCompletionService;
import org.uber.popug.task.tracker.service.TaskMembershipCheckingService;

@RequiredArgsConstructor
public class TaskCompletionServiceImpl implements TaskCompletionService {

    private final TaskMembershipCheckingService taskMembershipCheckingService;
    private final TaskRepository taskRepository;

    @Override
    public void completeTask(TaskForCompletionPublic task) {
        taskMembershipCheckingService.checkCompletionValid(task)
                .ifPresent(taskRepository::complete);
    }

}
