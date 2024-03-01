package org.uber.popug.task.tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.uber.popug.task.tracker.exception.NotImplementedException;
import org.uber.popug.task.tracker.rest.generated.api.TasksApi;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;

@RestController
public class TaskTrackerController implements TasksApi {
    @Override
    public ResponseEntity<PostTasksResponseDto> postTasks(PostTasksRequestDto postTasksRequestDto) {
        throw new NotImplementedException(TaskTrackerController.class, "postTasks");
    }
}
