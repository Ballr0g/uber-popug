package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.service.TransactionalAccountingService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;

@RequiredArgsConstructor
public class UserAccountBillingServiceImpl implements UserAccountBillingService {

    private final TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper;
    private final UserAccountMembershipCheckingService accountMembershipCheckingService;
    private final TransactionalAccountingService transactionalAccountingService;

    @Override
    public void billUserForTaskAssignment(TaskCreatedEvent taskCreatedEvent) {
        /*
         * Algorithm:
         * 1) Map Kafka TaskCreatedEvent into a task + assignee business type.
         * 2) Find the task and its assignee in the DB:
         *      1) Get TaskEntity from DB.
         *      2) Make sure the requested and actual assignees match.
         *      3) Make sure the user with requested id exists with an account (composite entity).
         * 3) Perform transactional accounting:
         *      1) Add task action to the immutable transactions log.
         *      2) Update user balance.
         */
        final var taskForBillingCandidate = tasksBusinessKafkaEventMapper.toBusiness(taskCreatedEvent);
        final var taskForBilling = accountMembershipCheckingService
                .retrieveTaskWithAssigneeIfRequestValid(taskForBillingCandidate);

        transactionalAccountingService.billForNewlyAssignedTask(taskForBilling);
    }

}
