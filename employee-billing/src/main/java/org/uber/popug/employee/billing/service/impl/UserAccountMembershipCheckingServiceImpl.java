package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.TaskForReassignment;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.task.creation.TaskAssignmentInfo;
import org.uber.popug.employee.billing.domain.task.completion.TaskCompletionInfo;
import org.uber.popug.employee.billing.domain.task.reassignment.TaskReassignmentInfo;
import org.uber.popug.employee.billing.domain.user.User;
import org.uber.popug.employee.billing.exception.TaskAssignmentMismatchException;
import org.uber.popug.employee.billing.exception.TaskNotFoundException;
import org.uber.popug.employee.billing.exception.UserNotFoundException;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.mapping.UsersPersistenceMapper;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.repository.UserRepository;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class UserAccountMembershipCheckingServiceImpl implements UserAccountMembershipCheckingService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TasksPersistenceMapper tasksPersistenceMapper;
    private final UsersPersistenceMapper usersPersistenceMapper;

    /**
     * Algorithm:
     * <ol>
     *     <li>Get TaskWithAssignee by requested public task id (retrieve actual assignee of the given task).</li>
     *     <li>Get assignee by requested public task id.</li>
     *     <li>Check that requested and actual assignees match, otherwise request data is invalid.</li>
     * </ol>
     *
     * @return A {@link TaskWithAssignee} object containing all the necessary data about the task and its assignee in
     * the system.
     */
    @Override
    public TaskWithAssignee retrieveTaskWithAssigneeIfRequestValid(TaskAssignmentInfo taskAssignmentInfo) {
        return retrieveTaskWithAssigneeByIdsIfPossible(
                taskAssignmentInfo.taskExtPublicId(),
                taskAssignmentInfo.assigneeExtPublicId()
        );
    }

    @Override
    public TaskForReassignment retrieveTaskForReassignmentIfRequestValid(TaskReassignmentInfo taskReassignmentInfo) {
        final var requestedTaskWithPreviousAssignee = retrieveTaskWithAssigneeByIdsIfPossible(
                taskReassignmentInfo.taskExtPublicId(),
                taskReassignmentInfo.previousAssigneeExtPublicId()
        );
        final var requestedNewTaskAssignee = retrieveAssigneeIfPossibleForPublicId(taskReassignmentInfo.newAssigneeExtPublicId());

        return new TaskForReassignment(requestedTaskWithPreviousAssignee, requestedNewTaskAssignee);
    }

    @Override
    public TaskWithAssignee retrieveTaskForCompletionIfRequestValid(TaskCompletionInfo taskCompletionInfo) {
       return retrieveTaskWithAssigneeByIdsIfPossible(
               taskCompletionInfo.taskExtPublicId(),
               taskCompletionInfo.assigneeExtPublicId()
       );
    }

    private TaskWithAssignee retrieveTaskWithAssigneeByIdsIfPossible(UUID publicTaskId, UUID publicAssigneeId) {
        final var requestedTaskWithActualAssignee = retrieveTaskWithAssigneeIfPossible(publicTaskId);
        final var requestedTaskAssignee = retrieveAssigneeIfPossibleForPublicId(publicAssigneeId);

        // Todo: ensure these 2 cases are impossible to co-exist:
        // 1) Assignee info arrived with a delay (actually the task has been reassigned, potentially multiple times).
        // 2) The task has never been assigned to that specific user.
        if (requestedAssigneeMismatchesActualAssignee(requestedTaskWithActualAssignee, requestedTaskAssignee)) {
            throw new TaskAssignmentMismatchException(publicTaskId, publicAssigneeId);
        }

        return requestedTaskWithActualAssignee;
    }

    private TaskWithAssignee retrieveTaskWithAssigneeIfPossible(UUID publicTaskId) {
        final var taskToAssigneeEntityForPublicIdOpt = taskRepository.findTaskToAssigneeByPublicTaskId(publicTaskId);

        if (taskToAssigneeEntityForPublicIdOpt.isEmpty()) {
            throw new TaskNotFoundException(publicTaskId);
        }
        final var taskToAssigneeEntityForPublicId = taskToAssigneeEntityForPublicIdOpt.get();

        return tasksPersistenceMapper.toBusiness(taskToAssigneeEntityForPublicId);
    }

    private User retrieveAssigneeIfPossibleForPublicId(UUID publicAssigneeId) {
        final var assigneeEntityForRequestedTaskOptional = userRepository.findByPublicId(publicAssigneeId);

        if (assigneeEntityForRequestedTaskOptional.isEmpty()) {
            throw new UserNotFoundException(publicAssigneeId);
        }
        final var assigneeEntityForRequestedTask = assigneeEntityForRequestedTaskOptional.get();

        return usersPersistenceMapper.toBusiness(assigneeEntityForRequestedTask);
    }

    private boolean requestedAssigneeMismatchesActualAssignee(
            TaskWithAssignee requestedTaskWithAssignee,
            User actualAssignee
    ) {
        return !Objects.equals(requestedTaskWithAssignee.assignee().extPublicId(), actualAssignee.extPublicId());
    }

}
