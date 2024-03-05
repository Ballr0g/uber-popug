package org.uber.popug.task.tracker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.task.tracker.domain.userrole.UserRole;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.entity.user.UserToRoleEntity;
import org.uber.popug.task.tracker.entity.userrole.UserRoleEntity;
import org.uber.popug.task.tracker.exception.NotImplementedException;
import org.uber.popug.task.tracker.repository.UserRepository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class JdbcClientUserRepository implements UserRepository {
    private static final String GET_USERS_FOR_ROLE_SQL = /*language=sql*/
            """
            SELECT u.ext_user_id, u.login, ur.role_id, ur.role_name
            FROM users u
            INNER JOIN users_to_user_roles uur ON u.ext_user_id = uur.user_id
            INNER JOIN user_roles ur ON uur.role_id = ur.role_id
            WHERE ur.role_name IN :roleNames
            """;
    private final JdbcClient jdbcClient;

    @Override
    public List<UserEntity> getDevelopers() {
        throw new NotImplementedException(JdbcClientUserRepository.class, "getDevelopers");
    }

    public List<UserEntity> getUsersOfRoles(Set<UserRole> expectedRoles) {
        throw new NotImplementedException(JdbcClientUserRepository.class, "getUsersOfRoles");
//        final var userIdsToRoles = jdbcClient.sql(GET_USERS_FOR_ROLE_SQL)
//                .param("roleNames", expectedRoles)
//                .query((ResultSet rs, int rowNumber) -> {
//                    return new UserToRoleEntity(
//                            rs.getObject("ext_user_id", UUID.class),
//                            rs.getString("login"),
//                            new UserRoleEntity(
//                                    rs.getLong("role_id"),
//                                    rs.getString("role_name")
//                            )
//                    );
//                })
//                .list();
    }
}
