package org.uber.popug.employee.billing.domain.task;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record Task(
        long id,
        @Nonnull UUID extPublicId,
        @Nonnull String description,
        @Nonnull Status status,
        long assignmentCost,
        long completionCost
) {
    public enum Status {
        OPEN,
        COMPLETED
    }

    public static Task replicate(
            @Nonnull TaskIdProvider taskIdProvider,
            @Nonnull TaskCostsProvider taskCostsProvider,
            @Nonnull TaskInfo taskInfo
    ) {
        return new Task(
                taskIdProvider.generateDbTaskId(),
                taskInfo.id(),
                taskInfo.description(),
                Status.OPEN,
                taskCostsProvider.calculateAssignmentCost(),
                taskCostsProvider.calculateCompletionCost()
        );
    }

}
