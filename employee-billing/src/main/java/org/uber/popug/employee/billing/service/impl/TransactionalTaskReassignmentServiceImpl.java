package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.employee.billing.domain.aggregates.TaskForReassignment;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.exception.technical.TaskUpdateFailedException;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.service.TransactionalTaskReassignmentService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;

@RequiredArgsConstructor
public class TransactionalTaskReassignmentServiceImpl implements TransactionalTaskReassignmentService {

    private final UserAccountBillingService userAccountBillingService;
    private final TaskRepository taskRepository;
    private final TasksPersistenceMapper tasksPersistenceMapper;

    @Override
    @Transactional
    public TaskWithAssignee performTransactionalReassignment(TaskForReassignment taskForReassignment) {
        final var taskWithNewAssignee = performTaskReassignment(taskForReassignment);
        userAccountBillingService.billUserForTaskReassignment(taskWithNewAssignee);

        return taskWithNewAssignee;
    }

    private TaskWithAssignee performTaskReassignment(TaskForReassignment taskForReassignment) {
        final var taskReassigned = taskForReassignment.taskWithCurrentAssignee().task();
        final var newAssignee = taskForReassignment.newAssignee();

        final var updatedTaskEntityOpt = taskRepository.updateTaskAssigneeById(taskReassigned.id(), newAssignee.id());
        final var updatedTaskEntity = updatedTaskEntityOpt.orElseThrow(
                () -> new TaskUpdateFailedException(taskForReassignment)
        );

        final var updatedTask = tasksPersistenceMapper.toBusiness(updatedTaskEntity);
        return new TaskWithAssignee(updatedTask, taskForReassignment.newAssignee());
    }

}
