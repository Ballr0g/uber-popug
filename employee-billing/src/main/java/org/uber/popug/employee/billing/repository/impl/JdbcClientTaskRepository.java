package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.task.TaskEntity;
import org.uber.popug.employee.billing.repository.TaskRepository;

@RequiredArgsConstructor
public class JdbcClientTaskRepository implements TaskRepository {

    private static final String GENERATE_NEXT_TASK_ID_SQL = /* language=postgresql */
            """
            SELECT NEXTVAL('EMPLOYEE_BILLING.tasks_id_seq')
            """;

    private static final String INSERT_BILLING_TASK_SQL = /* language=postgresql */
            """
            INSERT INTO EMPLOYEE_BILLING.TASKS
                (id, ext_public_id, assignee_id, description, assignment_cost, completion_cost)
            VALUES
                (:id, :extPublicId, :assigneeId, :description, :assignmentCost, :completionCost)
            """;

    private final JdbcClient jdbcClient;

    @Override
    public long generateNextDbTaskId() {
        return jdbcClient.sql(GENERATE_NEXT_TASK_ID_SQL)
                .query(Long.class)
                .single();
    }

    @Override
    public int saveReplicated(TaskEntity task) {
        return jdbcClient.sql(INSERT_BILLING_TASK_SQL)
                .param("id", task.id())
                .param("extPublicId", task.extPublicId())
                .param("assigneeId", task.assigneeId())
                .param("description", task.description())
                .param("assignmentCost", task.assignmentCost())
                .param("completionCost", task.completionCost())
                .update();
    }
}
