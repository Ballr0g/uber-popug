package org.uber.popug.employee.billing.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TaskNotFoundException extends RuntimeException {
    private final UUID publicUserId;
    private TaskNotFoundException(String message, UUID publicUserId) {
        super(message);
        this.publicUserId = publicUserId;
    }

    public static TaskNotFoundException forPublicTaskId(UUID publicTaskId) {
        return new TaskNotFoundException(
                "The task with public ID %s does not exist.".formatted(publicTaskId),
                publicTaskId
        );
    }
}
