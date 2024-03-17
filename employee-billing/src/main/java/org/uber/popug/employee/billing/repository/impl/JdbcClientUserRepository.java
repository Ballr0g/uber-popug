package org.uber.popug.employee.billing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.employee.billing.entity.user.UserEntity;
import org.uber.popug.employee.billing.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JdbcClientUserRepository implements UserRepository {

    private static final String FIND_USER_BY_PUBLIC_ID_SQL = /* language=postgresql */
            """
            SELECT id, ext_public_id, login
            FROM EMPLOYEE_BILLING.USERS
            WHERE ext_public_id = :extPublicUserId
            """;

    private final JdbcClient jdbcClient;

    @Override
    public Optional<UserEntity> findByPublicId(UUID publicUserId) {
        return jdbcClient.sql(FIND_USER_BY_PUBLIC_ID_SQL)
                .param("extPublicUserId", publicUserId)
                .query(UserEntity.class)
                .optional();
    }

}
