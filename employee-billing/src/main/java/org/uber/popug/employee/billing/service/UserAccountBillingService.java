package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.billing.creation.TaskForBillingAssignment;

public interface UserAccountBillingService {

    void billUserForTaskAssignment(TaskForBillingAssignment taskForBillingAssignment);

}
