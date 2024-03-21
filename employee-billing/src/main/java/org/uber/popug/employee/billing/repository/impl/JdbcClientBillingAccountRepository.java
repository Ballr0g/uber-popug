package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.billing.account.BillingAccountEntity;
import org.uber.popug.employee.billing.repository.BillingAccountRepository;

import java.util.List;
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

    private static final String UPDATE_BILLING_ACCOUNT_BALANCE_RESET_POSITIVE = /* language=postgresql */
            """
            UPDATE EMPLOYEE_BILLING.billing_accounts
            SET current_total = 0
            WHERE current_total > 0
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

    @Override
    public Optional<BillingAccountEntity> increaseAccountBalanceByOwnerUserId(long ownerUserId, long toAdd) {
        return jdbcClient.sql(UPDATE_BILLING_ACCOUNT_BALANCE_BY_USER_ID_SQL)
                .param("ownerUserId", ownerUserId)
                .param("toSubtract", 0L)
                .param("toAdd", toAdd)
                .query(BillingAccountEntity.class)
                .optional();
    }

    @Override
    public List<BillingAccountEntity> resetPositiveBalance() {
        return jdbcClient.sql(UPDATE_BILLING_ACCOUNT_BALANCE_RESET_POSITIVE)
                .query(BillingAccountEntity.class)
                .list();
    }

}
