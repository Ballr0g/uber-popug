package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.billing.cycle.BillingCycleEntity;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class JdbcClientBillingCycleRepository implements BillingCycleRepository {

    private static final String SELECT_ACTIVE_BILLING_CYCLE_SQL = /* language=postgresql */
            """
            SELECT id, public_id, start_date, end_date, state
            FROM EMPLOYEE_BILLING.billing_cycles
            WHERE state = 'ACTIVE'
            """;

    private final JdbcClient jdbcClient;

    @Override
    public Optional<BillingCycleEntity> findActiveBillingCycle() {
        return jdbcClient.sql(SELECT_ACTIVE_BILLING_CYCLE_SQL)
                .query(BillingCycleEntity.class)
                .optional();
    }
}
