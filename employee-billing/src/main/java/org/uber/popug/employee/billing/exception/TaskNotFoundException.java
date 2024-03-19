package org.uber.popug.employee.billing.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TaskNotFoundException extends RuntimeException {

    private static final String TASK_NOT_FOUND_FOR_PUBLIC_ID_MESSAGE_TEMPLATE
            = "The task with public ID %s does not exist.";

    private final UUID publicUserId;

    public TaskNotFoundException(UUID publicUserId) {
        super(TASK_NOT_FOUND_FOR_PUBLIC_ID_MESSAGE_TEMPLATE.formatted(publicUserId));

        this.publicUserId = publicUserId;
    }

}
