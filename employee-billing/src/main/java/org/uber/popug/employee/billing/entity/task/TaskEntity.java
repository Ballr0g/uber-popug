package org.uber.popug.employee.billing.entity.task;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

public record TaskEntity(
        long id,
        @Nonnull UUID extPublicId,
        long assigneeId,
        @Nonnull String description,
        @Nonnull Status status,
        long assignmentCost,
        long completionCost
) {
    @Getter
    @RequiredArgsConstructor
    public enum Status {
        OPEN("OPEN"),
        COMPLETED("COMPLETED");

        @Nonnull
        private final String persistenceValue;
    }
}
