package org.uber.popug.employee.billing.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {

    private static final String USER_NOT_FOUND_FOR_PUBLIC_ID_MESSAGE_TEMPLATE
            = "The user with public ID %s does not exist.";

    private final UUID publicUserId;

    public UserNotFoundException(UUID publicUserId) {
        super(USER_NOT_FOUND_FOR_PUBLIC_ID_MESSAGE_TEMPLATE.formatted(publicUserId));
        this.publicUserId = publicUserId;
    }

}
