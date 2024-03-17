package org.uber.popug.employee.billing.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final UUID publicUserId;
    private UserNotFoundException(String message, UUID publicUserId) {
        super(message);
        this.publicUserId = publicUserId;
    }

    public static UserNotFoundException forPublicUserId(UUID publicUserId) {
        return new UserNotFoundException(
                "The user with public ID %s does not exist.".formatted(publicUserId),
                publicUserId
        );
    }
}
