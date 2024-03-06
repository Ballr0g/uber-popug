package org.uber.popug.task.tracker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletion;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.repository.TaskRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JdbcClientTaskRepository implements TaskRepository {

    private static final String INSERT_TASK_SQL = /*language=sql*/
            """
            INSERT INTO tasks
                (task_id, public_task_id, assignee_id, description)
            VALUES
                (:taskId, :publicTaskId, :assigneeId, :description)
            """;

    private static final String FIND_TASK_BY_PUBLIC_ID_SQL = /*language=sql*/
            """
            SELECT t.task_id, t.public_task_id, t.assignee_id, t.description, t.status
            FROM tasks t
            WHERE t.public_task_id = :publicTaskId
            """;

    private static final String COMPLETE_TASK_SQL = /*language=sql*/
            """
            UPDATE tasks
            SET status = 'COMPLETED'
            WHERE task_id = :taskId AND assignee_id = :assigneeId
            """;

    private static final String GENERATE_NEXT_TASK_ID_SQL = /*language=sql*/
            """
            SELECT NEXTVAL('tasks_task_id_seq')
            """;

    private final JdbcClient jdbcClient;

    @Override
    public long generateNextDbTaskId() {
        return jdbcClient.sql(GENERATE_NEXT_TASK_ID_SQL)
                .query(Long.class)
                .single();
    }

    @Override
    public int save(Task task) {
        return jdbcClient.sql(INSERT_TASK_SQL)
                .param("taskId", task.taskId())
                .param("publicTaskId", task.publicTaskId())
                .param("assigneeId", task.assignee().assigneeId())
                .param("description", task.description())
                .update();
    }

    @Override
    public int complete(TaskForCompletion task) {
        return jdbcClient.sql(COMPLETE_TASK_SQL)
                .param("taskId", task.taskId())
                .param("assigneeId", task.assigneeId())
                .update();
    }

    @Override
    public Optional<TaskEntity> findByPublicId(UUID publicTaskId) {
        return jdbcClient.sql(FIND_TASK_BY_PUBLIC_ID_SQL)
                .param("publicTaskId", publicTaskId)
                .query(TaskEntity.class)
                .optional();
    }

}
