package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.composite.TaskToAssigneeEntity;
import org.uber.popug.employee.billing.entity.task.TaskEntity;
import org.uber.popug.employee.billing.entity.user.UserEntity;
import org.uber.popug.employee.billing.repository.TaskRepository;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

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

    private static final String FIND_TASK_TO_USER_ENTITY_BY_PUBLIC_TASK_ID_SQL = /* language=postgresql */
            """
            SELECT t.id AS task_id, t.ext_public_id AS ext_public_task_id, t.assignee_id AS assignee_id,
                   t.description AS task_description, t.status AS task_status,
                   t.assignment_cost AS task_assignment_cost, t.completion_cost AS task_completion_cost,
                   u.ext_public_id AS ext_public_assignee_id, u.login AS assignee_login
            FROM EMPLOYEE_BILLING.TASKS t
            INNER JOIN EMPLOYEE_BILLING.USERS u ON u.id = t.assignee_id
            WHERE t.ext_public_id = :extPublicTaskId
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

    @Override
    public Optional<TaskToAssigneeEntity> findTaskToAssigneeByPublicTaskId(UUID publicTaskId) {
        return jdbcClient.sql(FIND_TASK_TO_USER_ENTITY_BY_PUBLIC_TASK_ID_SQL)
                .param("extPublicTaskId", publicTaskId)
                .query((ResultSet rs, int rowNum) ->
                        new TaskToAssigneeEntity(
                                new TaskEntity(
                                        rs.getLong("task_id"),
                                        rs.getObject("ext_public_task_id", UUID.class),
                                        rs.getLong("assignee_id"),
                                        rs.getString("task_description"),
                                        TaskEntity.Status.valueOf(rs.getString("task_status")),
                                        rs.getLong("task_assignment_cost"),
                                        rs.getLong("task_completion_cost")
                                ),
                                new UserEntity(
                                        rs.getLong("assignee_id"),
                                        rs.getObject("ext_public_assignee_id", UUID.class),
                                        rs.getString("assignee_login")
                                ))
                )
                .optional();
    }
}
