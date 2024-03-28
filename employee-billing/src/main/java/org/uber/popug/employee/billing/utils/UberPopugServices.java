package org.uber.popug.employee.billing.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UberPopugServices {

    EMPLOYEE_BILLING("uber-popug.employee-billing");

    private final String name;

}
