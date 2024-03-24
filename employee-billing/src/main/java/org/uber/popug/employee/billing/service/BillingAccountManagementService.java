package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.BillingAccountWithOwner;
import org.uber.popug.employee.billing.domain.aggregates.PaymentDataWithUser;

public interface BillingAccountManagementService {

    BillingAccountWithOwner debitFromUser(PaymentDataWithUser paymentDataWithUser);

    BillingAccountWithOwner payToUser(PaymentDataWithUser paymentDataWithUser);

}
