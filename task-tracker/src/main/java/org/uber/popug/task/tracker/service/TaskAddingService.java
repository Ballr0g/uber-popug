package org.uber.popug.task.tracker.service;

import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskResponseDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskV2RequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskV2ResponseDto;

public interface TaskAddingService {

    PostAddTaskResponseDto addNewTask(PostAddTaskRequestDto postTasksRequestDto);

    PostAddTaskV2ResponseDto addNewTask(PostAddTaskV2RequestDto postTasksV2RequestDto);

}
