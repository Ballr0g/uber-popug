package org.uber.popug.task.tracker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.repository.TaskRepository;

@RequiredArgsConstructor
public class JdbcClientTaskRepository implements TaskRepository {

    private static final String INSERT_TASK_SQL = /*language=sql*/
            """
            INSERT INTO tasks
                (public_task_id, assignee_id, description)
            VALUES
                (:publicTaskId, :assigneeId, :description)
            """;

    private final JdbcClient jdbcClient;

    @Override
    public int save(Task task) {
        return jdbcClient.sql(INSERT_TASK_SQL)
                .param("publicTaskId", task.publicTaskId())
                .param("assigneeId", task.assignee().assigneeId())
                .param("description", task.description())
                .update();
    }

}
