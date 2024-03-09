package org.uber.popug.task.tracker.entity.task;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

public record TaskEntity(
        long id,
        @Nonnull UUID publicId,
        long assigneeId,
        @Nonnull String description,
        @Nonnull Status status
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
