package org.uber.popug.task.tracker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.uber.popug.task.tracker.domain.userrole.UserRole;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.entity.user.UserToRoleEntity;
import org.uber.popug.task.tracker.entity.userrole.UserRoleEntity;
import org.uber.popug.task.tracker.repository.UserRepository;

import java.sql.ResultSet;
import java.util.ArrayList;
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
            SELECT u.user_id, u.ext_public_user_id, u.login, ur.role_id, ur.role_name
            FROM users u
            INNER JOIN users_to_user_roles uur ON u.user_id = uur.user_id
            INNER JOIN user_roles ur ON uur.role_id = ur.role_id
            WHERE ur.role_name IN (:roleNames)
            """;
    private final JdbcClient jdbcClient;

    @Override
    public List<UserEntity> getDevelopers() {
        return getUsersOfRoles(Set.of(UserRole.DEVELOPER));
    }

    private List<UserEntity> getUsersOfRoles(Set<UserRole> expectedRoles) {
        final var usersToRoleEntries = selectUserToRolesEntriesForRolesFromDb(expectedRoles);

        Map<Long, UserEntity> usersWithRoles = new HashMap<>();
        for (var userToRole : usersToRoleEntries) {
            if (!usersWithRoles.containsKey(userToRole.userId())) {
                addUserToMap(usersWithRoles, userToRole);
            } else {
                addUserRoleToMap(usersWithRoles, userToRole);
            }
        }

        return new ArrayList<>(usersWithRoles.values());
    }

    private List<UserToRoleEntity> selectUserToRolesEntriesForRolesFromDb(Set<UserRole> expectedRoles) {
        return jdbcClient.sql(GET_USERS_FOR_ROLE_SQL)
                .param("roleNames", expectedRoles.stream().map(UserRole::getRoleName).toList())
                .query((ResultSet rs, int rowNumber) -> new UserToRoleEntity(
                        rs.getLong("user_id"),
                        rs.getObject("ext_public_user_id", UUID.class),
                        rs.getString("login"),
                        new UserRoleEntity(
                                rs.getLong("role_id"),
                                rs.getString("role_name")
                        )
                ))
                .list();
    }

    private static void addUserToMap(Map<Long, UserEntity> usersWithRoles, UserToRoleEntity newUser) {
        final var rolesSet = new HashSet<UserRoleEntity>();
        rolesSet.add(newUser.userRoleEntity());

        usersWithRoles.put(
                newUser.userId(),
                new UserEntity(
                        newUser.userId(),
                        newUser.extPublicUserId(),
                        newUser.userLogin(),
                        rolesSet
                )
        );
    }

    private static void addUserRoleToMap(Map<Long, UserEntity> usersWithRoles, UserToRoleEntity userWithNewRole) {
        final var userForNewRole = usersWithRoles.get(userWithNewRole.userId());
        final var rolesOfUserUpdated = userForNewRole.roles();
        rolesOfUserUpdated.add(userWithNewRole.userRoleEntity());
    }
}
