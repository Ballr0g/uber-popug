package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.billing.account.BillingAccount;
import org.uber.popug.employee.billing.mapping.BillingAccountsPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;
import org.uber.popug.employee.billing.service.PaymentService;

import java.util.List;

@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BillingAccountRepository billingAccountRepository;
    private final BillingAccountsPersistenceMapper billingAccountsPersistenceMapper;

    @Override
    public List<BillingAccount> payUsersForPositiveBalance() {
        final var accountsPaid = billingAccountRepository.resetPositiveBalance();
        return accountsPaid.stream().map(billingAccountsPersistenceMapper::toBusiness).toList();
    }

}
