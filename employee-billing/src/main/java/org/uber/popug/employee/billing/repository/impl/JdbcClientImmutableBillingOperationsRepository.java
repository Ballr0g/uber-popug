package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.billing.operation.BillingOperationEntity;
import org.uber.popug.employee.billing.repository.ImmutableBillingOperationsRepository;

import java.util.Map;

@RequiredArgsConstructor
public class JdbcClientImmutableBillingOperationsRepository implements ImmutableBillingOperationsRepository {

    private static final String GENERATE_NEXT_BILLING_OPERATION_ID_SQL = /* language=postgresql */
            """
            SELECT NEXTVAL('EMPLOYEE_BILLING.billing_operations_id_seq')
            """;

    private static final String INSERT_BILLING_OPERATION_LOG_ENTRY_SQL = /* language=postgresql */
            """
            INSERT INTO EMPLOYEE_BILLING.BILLING_OPERATIONS
                (id, public_id, owner_user_id, description, credit, debit, billing_cycle_id)
            VALUES
                (:id, :publicId, :ownerUserId, :description, :credit, :debit, :billingCycleId)
            """;

    private final JdbcClient jdbcClient;

    @Override
    public long generateNextDbBillingOperationId() {
        return jdbcClient.sql(GENERATE_NEXT_BILLING_OPERATION_ID_SQL)
                .query(Long.class)
                .single();
    }

    @Override
    public int appendBillingOperationEntry(BillingOperationEntity billingOperation) {
        return jdbcClient.sql(INSERT_BILLING_OPERATION_LOG_ENTRY_SQL)
                .params(
                        Map.ofEntries(
                                Map.entry("id", billingOperation.id()),
                                Map.entry("publicId", billingOperation.publicId()),
                                Map.entry("ownerUserId", billingOperation.ownerUserId()),
                                Map.entry("description", billingOperation.description()),
                                Map.entry("credit", billingOperation.credit()),
                                Map.entry("debit", billingOperation.debit()),
                                Map.entry("billingCycleId", billingOperation.billingCycleId())
                        )
                )
                .update();
    }

}
