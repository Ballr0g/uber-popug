package org.uber.popug.employee.billing.domain.task;

public interface TaskCostsProvider {

    long calculateAssignmentCost();

    long calculateCompletionCost();

}
