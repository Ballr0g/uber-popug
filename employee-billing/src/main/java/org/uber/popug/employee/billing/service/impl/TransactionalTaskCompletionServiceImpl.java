package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.exception.technical.TaskCompletionFailedException;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.service.TransactionalTaskCompletionService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;

@RequiredArgsConstructor
public class TransactionalTaskCompletionServiceImpl implements TransactionalTaskCompletionService {

    private final UserAccountBillingService userAccountBillingService;
    private final TaskRepository taskRepository;
    private final TasksPersistenceMapper tasksPersistenceMapper;

    @Override
    @Transactional
    public TaskWithAssignee performTransactionalCompletion(TaskWithAssignee taskForCompletion) {
        final var completedTask = performTaskCompletion(taskForCompletion);
        userAccountBillingService.payUserForTaskCompletion(completedTask);

        return completedTask;
    }

    private TaskWithAssignee performTaskCompletion(TaskWithAssignee taskForCompletion) {
        final var completedTaskEntityOpt = taskRepository.completeTaskById(taskForCompletion.task().id());
        final var completedTaskEntity = completedTaskEntityOpt.orElseThrow(
                () -> new TaskCompletionFailedException(taskForCompletion)
        );

        final var completedTask = tasksPersistenceMapper.toBusiness(completedTaskEntity);
        return new TaskWithAssignee(completedTask, taskForCompletion.assignee());
    }
}
