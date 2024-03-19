package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;

public interface UserAccountBillingService {

    void billUserForTaskAssignment(TaskCreatedEvent taskCreatedEvent);

}
