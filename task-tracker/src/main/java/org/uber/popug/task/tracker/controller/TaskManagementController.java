package org.uber.popug.task.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.rest.generated.api.TasksCreationApi;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;
import org.uber.popug.task.tracker.service.TaskAddingService;
import org.uber.popug.task.tracker.service.TaskReassignmentService;

@RestController
@RequiredArgsConstructor
public class TaskManagementController implements TasksCreationApi {

    private final TaskAddingService taskAddingService;

    @Override
    public ResponseEntity<PostTasksResponseDto> postTasks(PostTasksRequestDto postTasksRequestDto) {
        return new ResponseEntity<>(taskAddingService.addNewTask(postTasksRequestDto), HttpStatus.CREATED);
    }

}
