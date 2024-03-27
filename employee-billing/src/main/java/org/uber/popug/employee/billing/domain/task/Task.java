package org.uber.popug.employee.billing.domain.task;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.uber.popug.employee.billing.domain.task.replication.TaskReplicationInfo;

import java.util.UUID;

public record Task(
        long id,
        @Nonnull UUID extPublicId,
        @Nullable String jiraId,
        @Nonnull String description,
        @Nonnull Status status,
        @Nonnull Costs costs
        ) {
    public enum Status {
        OPEN,
        COMPLETED
    }

    public record Costs(
            long assignmentCost,
            long completionCost
    ) {
    }

    public static Task replicateNoJiraId(
            @Nonnull TaskIdProvider taskIdProvider,
            @Nonnull TaskCostsProvider taskCostsProvider,
            @Nonnull TaskReplicationInfo taskReplicationInfo
    ) {
        return new Task(
                taskIdProvider.generateDbTaskId(),
                taskReplicationInfo.id(),
                null,
                taskReplicationInfo.description(),
                Status.OPEN,
                new Costs(
                        taskCostsProvider.calculateAssignmentCost(),
                        taskCostsProvider.calculateCompletionCost()
                )
        );
    }

}
