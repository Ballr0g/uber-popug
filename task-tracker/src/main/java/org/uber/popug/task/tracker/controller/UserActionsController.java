package org.uber.popug.task.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.domain.task.completion.TaskForCompletionPublic;
import org.uber.popug.task.tracker.rest.generated.api.UsersApi;
import org.uber.popug.task.tracker.rest.generated.model.PatchTasksCompleteResponseDto;
import org.uber.popug.task.tracker.service.TaskCompletionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserActionsController implements UsersApi {

    private final TaskCompletionService taskCompletionService;

    @Override
    public ResponseEntity<PatchTasksCompleteResponseDto> completeTask(UUID userId, UUID taskId) {
        taskCompletionService.completeTask(new TaskForCompletionPublic(taskId, userId));

        return ResponseEntity.ok(
                new PatchTasksCompleteResponseDto()
                        .taskAssigneeId(userId)
                        .taskId(taskId)
        );
    }

}
