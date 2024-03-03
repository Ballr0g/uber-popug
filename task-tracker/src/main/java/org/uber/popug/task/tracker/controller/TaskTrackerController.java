package org.uber.popug.task.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.rest.generated.api.TasksApi;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;
import org.uber.popug.task.tracker.service.TaskTrackerService;

@RestController
@RequiredArgsConstructor
public class TaskTrackerController implements TasksApi {
    private final TaskTrackerService taskTrackerService;
    @Override
    public ResponseEntity<PostTasksResponseDto> postTasks(PostTasksRequestDto postTasksRequestDto) {
        return new ResponseEntity<>(taskTrackerService.addNewTask(postTasksRequestDto), HttpStatus.CREATED);
    }
}
