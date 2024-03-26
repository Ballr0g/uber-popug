package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedEventV1;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.service.TaskAssignmentService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;

@RequiredArgsConstructor
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper;
    private final UserAccountMembershipCheckingService accountMembershipCheckingService;
    private final UserAccountBillingService userAccountBillingService;

    @Override
    public void handleTaskAssignment(TaskCreatedEventV1 taskCreatedEventV1) {
        final var taskForBillingCandidate = tasksBusinessKafkaEventMapper.toBusiness(taskCreatedEventV1);
        final var taskForBilling = accountMembershipCheckingService
                .retrieveTaskWithAssigneeIfRequestValid(taskForBillingCandidate);

        userAccountBillingService.billUserForTaskAssignment(taskForBilling);
    }

}
