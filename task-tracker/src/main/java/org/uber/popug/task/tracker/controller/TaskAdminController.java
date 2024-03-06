package org.uber.popug.task.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.uber.popug.task.tracker.rest.generated.api.TasksAdminApi;
import org.uber.popug.task.tracker.service.TaskReassignmentService;

@Controller
@RequiredArgsConstructor
public class TaskAdminController implements TasksAdminApi {

    private final TaskReassignmentService taskReassignmentService;

    @Override
    public ResponseEntity<Void> reassignAllTasks() {
        taskReassignmentService.reassignAllTasks();

        return ResponseEntity.ok().build();
    }

}
