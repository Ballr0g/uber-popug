package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.billing.account.BillingAccountEntity;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class JdbcClientBillingAccountRepository implements BillingAccountRepository {

    private static final String UPDATE_BILLING_ACCOUNT_BALANCE_BY_USER_ID_SQL = /* language=postgresql */
        """
        UPDATE EMPLOYEE_BILLING.billing_accounts
        SET current_total = current_total - :toSubtract + :toAdd
        WHERE owner_user_id = :ownerUserId
        RETURNING id, public_id, owner_user_id, current_total
        """;

    private final JdbcClient jdbcClient;

    @Override
    public Optional<BillingAccountEntity> subtractAccountBalanceByOwnerUser(long ownerUserId, long toSubtract) {
        return jdbcClient.sql(UPDATE_BILLING_ACCOUNT_BALANCE_BY_USER_ID_SQL)
                .param("ownerUserId", ownerUserId)
                .param("toSubtract", toSubtract)
                .param("toAdd", 0L)
                .query(BillingAccountEntity.class)
                .optional();
    }

}
