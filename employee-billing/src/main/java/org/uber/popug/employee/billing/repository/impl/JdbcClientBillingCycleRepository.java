package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.billing.cycle.BillingCycleEntity;
import org.uber.popug.employee.billing.repository.BillingCycleRepository;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcClientBillingCycleRepository implements BillingCycleRepository {

    private static final String GENERATE_NEXT_BILLING_CYCLE_ID_SQL = /* language=postgresql */
            """
            SELECT NEXTVAL('EMPLOYEE_BILLING.billing_cycles_id_seq')
            """;

    private static final String INSERT_BILLING_CYCLE_FOR_RANGE_SQL = /* language=postgresql */
            """
            INSERT INTO EMPLOYEE_BILLING.billing_cycles
                (id, public_id, start_date, end_date)
            VALUES
                (:id, :publicId, :startDate, :endDate)
            """;

    private static final String SELECT_ACTIVE_BILLING_CYCLE_SQL = /* language=postgresql */
            """
            SELECT id, public_id, start_date, end_date, state
            FROM EMPLOYEE_BILLING.billing_cycles
            WHERE state = 'ACTIVE'
            """;


    private final JdbcClient jdbcClient;

    @Override
    public long generateNextDbBillingCycleId() {
        return jdbcClient.sql(GENERATE_NEXT_BILLING_CYCLE_ID_SQL)
                .query(Long.class)
                .single();
    }

    @Override
    public int insertActiveBillingCycle(BillingCycleEntity billingCycleEntity) {
        return jdbcClient.sql(INSERT_BILLING_CYCLE_FOR_RANGE_SQL)
                .params(Map.ofEntries(
                        Map.entry("id", billingCycleEntity.id()),
                        Map.entry("publicId", billingCycleEntity.publicId()),
                        Map.entry("startDate", billingCycleEntity.startDate()),
                        Map.entry("endDate", billingCycleEntity.endDate())
                ))
                .update();
    }

    @Override
    public Optional<BillingCycleEntity> findActiveBillingCycle() {
        return jdbcClient.sql(SELECT_ACTIVE_BILLING_CYCLE_SQL)
                .query(BillingCycleEntity.class)
                .optional();
    }
}
