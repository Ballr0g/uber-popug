package org.uber.popug.employee.billing.domain.task.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.task.TaskCostsProvider;

import java.util.random.RandomGenerator;

@RequiredArgsConstructor
public class RandomTaskCostsProvider implements TaskCostsProvider {
    private static final long MIN_ASSIGNMENT_COST = 10L;
    private static final long MAX_ASSIGNMENT_COST = 20L;

    private static final long MIN_COMPLETION_COST = 20L;
    private static final long MAX_COMPLETION_COST = 40L;

    private static final long EXCLUSIVE_BOUND_OFFSET = 1L;

    private final RandomGenerator randomGenerator;

    @Override
    public long calculateAssignmentCost() {
        return randomGenerator.nextLong(MIN_ASSIGNMENT_COST, MAX_ASSIGNMENT_COST + EXCLUSIVE_BOUND_OFFSET);
    }

    @Override
    public long calculateCompletionCost() {
        return randomGenerator.nextLong(MIN_COMPLETION_COST, MAX_COMPLETION_COST + EXCLUSIVE_BOUND_OFFSET);
    }

}
