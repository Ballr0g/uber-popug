package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;

public interface TaskTrackerService {
    PostTasksResponseDto addNewTask(PostTasksRequestDto postTasksRequestDto);
}
