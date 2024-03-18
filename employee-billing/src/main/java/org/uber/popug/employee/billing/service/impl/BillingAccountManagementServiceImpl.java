package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.BillingAccountWithOwner;
import org.uber.popug.employee.billing.domain.aggregates.PaymentDataWithUser;
import org.uber.popug.employee.billing.domain.billing.account.BillingAccount;
import org.uber.popug.employee.billing.exception.technical.BillingAccountUpdateFailedException;
import org.uber.popug.employee.billing.mapping.BillingAccountsPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;
import org.uber.popug.employee.billing.service.BillingAccountManagementService;

@RequiredArgsConstructor
public class BillingAccountManagementServiceImpl implements BillingAccountManagementService {

    private final BillingAccountRepository billingAccountRepository;
    private final BillingAccountsPersistenceMapper billingAccountsPersistenceMapper;

    @Override
    public BillingAccountWithOwner payUserForInitialAssignment(PaymentDataWithUser paymentDataWithUser) {
        final var updatedBillingAccount = subtractInitialAssignmentIfPossible(paymentDataWithUser);
        return new BillingAccountWithOwner(updatedBillingAccount, paymentDataWithUser.relatedUser());
    }

    private BillingAccount subtractInitialAssignmentIfPossible(PaymentDataWithUser paymentDataWithUser) {
        final var updatedBillingAccountEntityOpt = billingAccountRepository.subtractAccountBalanceByOwnerUser(
                paymentDataWithUser.relatedUser().id(),
                paymentDataWithUser.data().credit()
        );

        final var updatedBillingAccountEntity = updatedBillingAccountEntityOpt.orElseThrow(
                () ->  new BillingAccountUpdateFailedException(
                        paymentDataWithUser.relatedUser().extPublicId(),
                        paymentDataWithUser.data()
                )
        );

        return billingAccountsPersistenceMapper.toBusiness(updatedBillingAccountEntity);
    }

}
