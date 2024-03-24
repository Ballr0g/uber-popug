package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskResponseDto;

public interface TaskAddingService {
    PostAddTaskResponseDto addNewTask(PostAddTaskRequestDto postTasksRequestDto);
}
