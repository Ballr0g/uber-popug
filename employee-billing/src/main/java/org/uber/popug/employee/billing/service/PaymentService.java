package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.billing.account.BillingAccount;

import java.util.List;

public interface PaymentService {

    List<BillingAccount> payUsersForPositiveBalance();

}
