package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.BillingAccountWithOwner;
import org.uber.popug.employee.billing.domain.aggregates.PaymentDataWithUser;

public interface BillingAccountManagementService {

    BillingAccountWithOwner payUserForInitialAssignment(PaymentDataWithUser paymentDataWithUser);

}
