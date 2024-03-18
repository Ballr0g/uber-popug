package org.uber.popug.employee.billing.domain.billing;

public record PaymentData(
        long credit,
        long debit
) {
}
