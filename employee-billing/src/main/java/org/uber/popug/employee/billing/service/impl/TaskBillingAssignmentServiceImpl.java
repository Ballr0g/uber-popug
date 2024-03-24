package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.task.Task;
import org.uber.popug.employee.billing.domain.task.TaskCostsProvider;
import org.uber.popug.employee.billing.domain.task.TaskIdProvider;
import org.uber.popug.employee.billing.domain.task.replication.TaskReplicationInfo;
import org.uber.popug.employee.billing.domain.user.User;
import org.uber.popug.employee.billing.exception.UserNotFoundException;
import org.uber.popug.employee.billing.mapping.UsersPersistenceMapper;
import org.uber.popug.employee.billing.repository.UserRepository;
import org.uber.popug.employee.billing.service.TaskBillingAssignmentService;

import java.util.UUID;

@RequiredArgsConstructor
public class TaskBillingAssignmentServiceImpl implements TaskBillingAssignmentService {

    private final UserRepository userRepository;
    private final UsersPersistenceMapper usersPersistenceMapper;
    private final TaskIdProvider taskIdProvider;
    private final TaskCostsProvider taskCostsProvider;

    @Override
    public TaskWithAssignee assembleTaskWithAssignee(TaskReplicationInfo taskReplicationInfo) {
        final var taskAssignee = getAssigneeIfExistsByPublicId(taskReplicationInfo.assigneeId());
        final var replicatedTask = Task.replicate(
                taskIdProvider,
                taskCostsProvider,
                taskReplicationInfo
        );

        return new TaskWithAssignee(replicatedTask, taskAssignee);
    }

    private User getAssigneeIfExistsByPublicId(UUID publicUserId) {
        final var taskAssigneeEntityOptional = userRepository.findByPublicId(publicUserId);
        if (taskAssigneeEntityOptional.isEmpty()) {
            throw new UserNotFoundException(publicUserId);
        }

        return usersPersistenceMapper.toBusiness(taskAssigneeEntityOptional.get());
    }

}
