package org.uber.popug.task.tracker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletion;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JdbcClientTaskRepository implements TaskRepository {

    private static final String INSERT_TASK_SQL = /*language=sql*/
            """
            INSERT INTO TASK_TRACKER.TASKS
                (id, public_id, assignee_id, description)
            VALUES
                (:id, :publicId, :assigneeId, :description)
            """;

    private static final String FIND_TASK_BY_PUBLIC_ID_SQL = /*language=sql*/
            """
            SELECT t.id, t.public_id, t.assignee_id, t.description, t.status
            FROM TASK_TRACKER.TASKS t
            WHERE t.public_id = :publicId
            """;

    private static final String FIND_TASK_BY_ID_SQL = /*language=sql*/
            """
            SELECT t.id, t.public_id, t.assignee_id, t.description, t.status
            FROM TASK_TRACKER.TASKS t
            WHERE t.id = :id
            """;

    private static final String COMPLETE_TASK_SQL = /*language=sql*/
            """
            UPDATE TASK_TRACKER.TASKS
            SET status = 'COMPLETED'
            WHERE id = :id AND assignee_id = :assigneeId
            """;

    private static final String GENERATE_NEXT_TASK_ID_SQL = /*language=sql*/
            """
            SELECT NEXTVAL('TASK_TRACKER.tasks_id_seq')
            """;

    private static final String SELECT_ALL_OPEN_TASKS_SQL = /*language=sql*/
            """
            SELECT t.id, t.public_id, t.assignee_id, t.description, t.status
            FROM TASK_TRACKER.TASKS t
            WHERE t.status = 'OPEN'
            """;

    private static final String REASSIGN_TASK_SQL = /*language=sql*/
            """
            UPDATE TASK_TRACKER.TASKS
            SET assignee_id = :assigneeId
            WHERE id = :id
            """;

    private static final String FIND_TASKS_BY_ASSIGNEE_ID_SQL = /*language=sql*/
            """
            SELECT t.id, t.public_id, t.assignee_id, t.description, t.status
            FROM TASK_TRACKER.TASKS t
            WHERE t.assignee_id = :assigneeId
            """;

    private final JdbcClient jdbcClient;

    @Override
    public long generateNextDbTaskId() {
        return jdbcClient.sql(GENERATE_NEXT_TASK_ID_SQL)
                .query(Long.class)
                .single();
    }

    @Override
    public int add(Task task) {
        return jdbcClient.sql(INSERT_TASK_SQL)
                .param("id", task.id())
                .param("publicId", task.publicId())
                .param("assigneeId", task.assignee().id())
                .param("description", task.description())
                .update();
    }

    @Override
    public int complete(TaskForCompletion task) {
        return jdbcClient.sql(COMPLETE_TASK_SQL)
                .param("id", task.taskId())
                .param("assigneeId", task.assigneeId())
                .update();
    }

    @Override
    public Optional<TaskEntity> findById(long taskId) {
        return jdbcClient.sql(FIND_TASK_BY_ID_SQL)
                .param("id", taskId)
                .query(TaskEntity.class)
                .optional();
    }

    @Override
    public Optional<TaskEntity> findByPublicId(UUID publicTaskId) {
        return jdbcClient.sql(FIND_TASK_BY_PUBLIC_ID_SQL)
                .param("publicId", publicTaskId)
                .query(TaskEntity.class)
                .optional();
    }

    @Override
    public List<TaskEntity> readAllTasks() {
        return jdbcClient.sql(SELECT_ALL_OPEN_TASKS_SQL)
                .query(TaskEntity.class)
                .list();
    }

    @Override
    public List<TaskEntity> findTasksByAssigneeId(long assigneeId) {
        return jdbcClient.sql(FIND_TASKS_BY_ASSIGNEE_ID_SQL)
                .param("assigneeId", assigneeId)
                .query(TaskEntity.class)
                .list();
    }

    @Override
    public int reassignTask(TaskEntity task, UserEntity newAssignee) {
        return jdbcClient.sql(REASSIGN_TASK_SQL)
                .param("assigneeId", newAssignee.id())
                .param("id", task.id())
                .update();
    }

}
