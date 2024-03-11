package org.uber.popug.task.tracker.domain.userrole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    DEVELOPER("developer"),
    ACCOUNTANT("accountant"),
    ADMINISTRATOR("administrator"),
    MANAGER("manager");

    private final String roleName;
}
