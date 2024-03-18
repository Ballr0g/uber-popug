package org.uber.popug.employee.billing.domain.billing;

public record PaymentData(
        long credit,
        long debit
) {

    public static PaymentData newCreditData(long credit) {
        return new PaymentData(credit, 0L);
    }

}
