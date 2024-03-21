package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.BillingAccountWithOwner;
import org.uber.popug.employee.billing.domain.aggregates.PaymentDataWithUser;
import org.uber.popug.employee.billing.domain.billing.account.BillingAccount;
import org.uber.popug.employee.billing.entity.billing.account.BillingAccountEntity;
import org.uber.popug.employee.billing.exception.technical.BillingAccountUpdateFailedException;
import org.uber.popug.employee.billing.mapping.BillingAccountsPersistenceMapper;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;
import org.uber.popug.employee.billing.service.BillingAccountManagementService;

import java.util.Optional;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class BillingAccountManagementServiceImpl implements BillingAccountManagementService {

    private final BillingAccountRepository billingAccountRepository;
    private final BillingAccountsPersistenceMapper billingAccountsPersistenceMapper;

    // Todo: rename, remove misleading debit
    @Override
    public BillingAccountWithOwner debitFromUser(PaymentDataWithUser paymentDataWithUser) {
        final var updatedBillingAccount = subtractInitialAssignmentIfPossible(paymentDataWithUser);
        return new BillingAccountWithOwner(updatedBillingAccount, paymentDataWithUser.relatedUser());
    }

    @Override
    public BillingAccountWithOwner payToUser(PaymentDataWithUser paymentDataWithUser) {
        final var updatedBillingAccount = payToUserIfPossible(paymentDataWithUser);
        return new BillingAccountWithOwner(updatedBillingAccount, paymentDataWithUser.relatedUser());
    }

    private BillingAccount subtractInitialAssignmentIfPossible(PaymentDataWithUser paymentDataWithUser) {
        return performBillingAccountUpdateIfPossible(
                paymentDataWithUser,
                () -> billingAccountRepository.subtractAccountBalanceByOwnerUser(
                        paymentDataWithUser.relatedUser().id(),
                        paymentDataWithUser.data().credit()
                )
        );
    }

    private BillingAccount payToUserIfPossible(PaymentDataWithUser paymentDataWithUser) {
        return performBillingAccountUpdateIfPossible(
                paymentDataWithUser,
                () -> billingAccountRepository.increaseAccountBalanceByOwnerUserId(
                        paymentDataWithUser.relatedUser().id(),
                        paymentDataWithUser.data().debit()
                )
        );
    }

    private BillingAccount performBillingAccountUpdateIfPossible(
            PaymentDataWithUser paymentDataWithUser,
            Supplier<Optional<BillingAccountEntity>> billingAccountPersistenceSupplier
    ) {
        final var updatedBillingAccountEntityOpt = billingAccountPersistenceSupplier.get();

        final var updatedBillingAccountEntity = updatedBillingAccountEntityOpt.orElseThrow(
                () ->  new BillingAccountUpdateFailedException(
                        paymentDataWithUser.relatedUser().extPublicId(),
                        paymentDataWithUser.data()
                )
        );

        return billingAccountsPersistenceMapper.toBusiness(updatedBillingAccountEntity);
    }

}
