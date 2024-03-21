package org.uber.popug.employee.billing.repository;

import org.uber.popug.employee.billing.entity.billing.account.BillingAccountEntity;

import java.util.List;
import java.util.Optional;

public interface BillingAccountRepository {

    Optional<BillingAccountEntity> subtractAccountBalanceByOwnerUser(long ownerUserId, long toSubtract);

    Optional<BillingAccountEntity> increaseAccountBalanceByOwnerUserId(long ownerUserId, long toAdd);

    List<BillingAccountEntity> resetPositiveBalance();

}
