package org.uber.popug.task.tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.exception.NotImplementedException;
import org.uber.popug.task.tracker.rest.generated.api.UsersApi;
import org.uber.popug.task.tracker.rest.generated.model.PatchTasksCompleteResponseDto;

import java.util.UUID;

@RestController
public class UserActionsController implements UsersApi {

    @Override
    public ResponseEntity<PatchTasksCompleteResponseDto> completeTask(UUID userId, UUID taskId) {
        throw new NotImplementedException(UserActionsController.class, "completeTask");
    }

}
