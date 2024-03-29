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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class JdbcClientUserRepository implements UserRepository {
    private static final String GET_USERS_FOR_ROLE_SQL = /*language=sql*/
            """
            SELECT u.id AS user_id, u.ext_public_id AS ext_public_user_id, u.login AS user_login,
                   ur.id AS user_role_id, ur.name AS user_role_name
            FROM TASK_TRACKER.USERS u
            INNER JOIN TASK_TRACKER.USERS_TO_USER_ROLES uur ON u.id = uur.user_id
            INNER JOIN TASK_TRACKER.USER_ROLES ur ON ur.id = uur.role_id
            WHERE ur.name IN (:roleNames)
            """;

    private static final String FIND_USER_BY_PUBLIC_ID_SQL = /*language=sql*/
            """
            SELECT u.id AS user_id, u.ext_public_id AS ext_public_user_id, u.login AS user_login,
                   ur.id AS user_role_id, ur.name AS user_role_name
            FROM TASK_TRACKER.USERS u
            INNER JOIN TASK_TRACKER.USERS_TO_USER_ROLES uur ON u.id = uur.user_id
            INNER JOIN TASK_TRACKER.USER_ROLES ur ON ur.id = uur.role_id
            WHERE u.ext_public_id = :extPublicUserId
            """;

    private static final String FIND_USER_BY_ID_SQL = /*language=sql*/
            """
            SELECT u.id AS user_id, u.ext_public_id AS ext_public_user_id, u.login AS user_login,
                   ur.id AS user_role_id, ur.name AS user_role_name
            FROM TASK_TRACKER.USERS u
            INNER JOIN TASK_TRACKER.USERS_TO_USER_ROLES uur ON u.id = uur.user_id
            INNER JOIN TASK_TRACKER.USER_ROLES ur ON ur.id = uur.role_id
            WHERE u.id = :userId
            """;

    private final JdbcClient jdbcClient;

    @Override
    public List<UserEntity> getDevelopers() {
        return getUsersOfRoles(Set.of(UserRole.DEVELOPER));
    }

    @Override
    public Optional<UserEntity> findByPublicId(UUID publicUserId) {
        final var userToRoles = getRolesPerUserByPublicId(publicUserId);
        return mergeRolesToUser(userToRoles);
    }

    @Override
    public Optional<UserEntity> findById(long userId) {
        final var userToRoles = getRolesPerUserById(userId);
        return mergeRolesToUser(userToRoles);
    }

    private List<UserToRoleEntity> getRolesPerUserById(long userId) {
        return jdbcClient.sql(FIND_USER_BY_ID_SQL)
                .param("userId", userId)
                .query((ResultSet rs, int rowNumber) -> new UserToRoleEntity(
                        rs.getLong("user_id"),
                        rs.getObject("ext_public_user_id", UUID.class),
                        rs.getString("user_login"),
                        new UserRoleEntity(
                                rs.getLong("user_role_id"),
                                rs.getString("user_role_name")
                        )
                ))
                .list();
    }

    private List<UserToRoleEntity> getRolesPerUserByPublicId(UUID publicUserId) {
        return jdbcClient.sql(FIND_USER_BY_PUBLIC_ID_SQL)
                .param("extPublicUserId", publicUserId)
                .query((ResultSet rs, int rowNumber) -> new UserToRoleEntity(
                        rs.getLong("user_id"),
                        rs.getObject("ext_public_user_id", UUID.class),
                        rs.getString("user_login"),
                        new UserRoleEntity(
                                rs.getLong("user_role_id"),
                                rs.getString("user_role_name")
                        )
                ))
                .list();
    }

    private Optional<UserEntity> mergeRolesToUser(List<UserToRoleEntity> userToRoles) {
        if (userToRoles.isEmpty()) {
            return Optional.empty();
        }

        final var userRoles = new HashSet<UserRoleEntity>();
        userToRoles.forEach(userToRoleEntity -> userRoles.add(userToRoleEntity.userRoleEntity()));
        final var user = userToRoles.get(0);

        return Optional.of(new UserEntity(
                user.userId(),
                user.extPublicUserId(),
                user.userLogin(),
                userRoles
        ));
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
                        rs.getString("user_login"),
                        new UserRoleEntity(
                                rs.getLong("user_role_id"),
                                rs.getString("user_role_name")
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
