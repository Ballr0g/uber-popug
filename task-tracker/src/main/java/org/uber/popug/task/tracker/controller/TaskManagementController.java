package org.uber.popug.task.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.rest.generated.api.TasksCreationApi;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskResponseDto;
import org.uber.popug.task.tracker.service.TaskAddingService;

@RestController
@RequiredArgsConstructor
public class TaskManagementController implements TasksCreationApi {

    private final TaskAddingService taskAddingService;

    @Override
    public ResponseEntity<PostAddTaskResponseDto> addTask(PostAddTaskRequestDto postTasksRequestDto) {
        return new ResponseEntity<>(taskAddingService.addNewTask(postTasksRequestDto), HttpStatus.CREATED);
    }

}
