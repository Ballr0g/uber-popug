package org.uber.popug.employee.billing.domain.task;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.util.UUID;

public record Task(
        long id,
        @Nonnull UUID extPublicId,
        @Nonnull String description,
        @Nonnull LocalDateTime creationDate,
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
                taskInfo.creationDate(),
                Status.OPEN,
                taskCostsProvider.calculateAssignmentCost(),
                taskCostsProvider.calculateCompletionCost()
        );
    }

}
