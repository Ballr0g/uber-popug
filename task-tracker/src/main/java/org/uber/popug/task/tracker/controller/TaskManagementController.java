package org.uber.popug.task.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.rest.generated.api.TasksCreationApi;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskResponseDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskV2RequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskV2ResponseDto;
import org.uber.popug.task.tracker.service.TaskAddingService;

@RestController
@RequiredArgsConstructor
public class TaskManagementController implements TasksCreationApi {

    private final TaskAddingService taskAddingService;

    @Override
    @Deprecated(since = "0.0.2", forRemoval = true)
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity<PostAddTaskResponseDto> addTask(PostAddTaskRequestDto postTasksRequestDto) {
        return new ResponseEntity<>(taskAddingService.addNewTask(postTasksRequestDto), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasRole('client_developer')")
    public ResponseEntity<PostAddTaskV2ResponseDto> addTaskV2(PostAddTaskV2RequestDto postAddTaskV2RequestDto) {
        return new ResponseEntity<>(taskAddingService.addNewTask(postAddTaskV2RequestDto), HttpStatus.CREATED);
    }

}
