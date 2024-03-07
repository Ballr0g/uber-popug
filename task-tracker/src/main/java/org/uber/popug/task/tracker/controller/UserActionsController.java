package org.uber.popug.task.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletionPublic;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.rest.generated.api.UserActionsApi;
import org.uber.popug.task.tracker.rest.generated.model.GetTasksByUserResponseDto;
import org.uber.popug.task.tracker.rest.generated.model.PatchTasksCompleteResponseDto;
import org.uber.popug.task.tracker.service.TaskCompletionService;
import org.uber.popug.task.tracker.service.TaskRetrievalService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserActionsController implements UserActionsApi {

    private final TaskCompletionService taskCompletionService;
    private final TaskRetrievalService taskRetrievalService;
    private final TasksDtoMapper tasksDtoMapper;

    @Override
    public ResponseEntity<PatchTasksCompleteResponseDto> completeTask(UUID userId, UUID taskId) {
        taskCompletionService.completeTask(new TaskForCompletionPublic(taskId, userId));

        return ResponseEntity.ok(
                new PatchTasksCompleteResponseDto()
                        .taskAssigneeId(userId)
                        .taskId(taskId)
        );
    }

    @Override
    public ResponseEntity<GetTasksByUserResponseDto> getTasksByUser(UUID userId) {
        final var tasksForUser = taskRetrievalService.getTasksForPublicAssigneeId(userId);

        final var taskDtoList = tasksDtoMapper.getTasksByUserResponseDtoListFromBusiness(tasksForUser);

        return ResponseEntity.ok(
                new GetTasksByUserResponseDto()
                        .userId(userId)
                        .userTasks(taskDtoList)
        );
    }

}
