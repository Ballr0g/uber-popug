package org.uber.popug.employee.billing.exception.technical;

import lombok.Getter;
import org.uber.popug.employee.billing.domain.billing.PaymentData;

import java.util.UUID;

@Getter
public class BillingAccountUpdateFailedException extends RuntimeException {

    private final UUID accountOwner;
    private final PaymentData failedPaymentData;

    public BillingAccountUpdateFailedException(UUID accountOwner, PaymentData failedPaymentData) {
        this.accountOwner = accountOwner;
        this.failedPaymentData = failedPaymentData;
    }

}
