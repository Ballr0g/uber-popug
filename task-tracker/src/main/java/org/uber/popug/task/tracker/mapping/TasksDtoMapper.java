package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.TaskForCreation;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;

@Mapper
public interface TasksDtoMapper {
    TaskForCreation postTaskRequestsDtoToBusiness(PostTasksRequestDto postTasksRequestDto);
    PostTasksResponseDto postTasksResponseDtoFromBusiness(Task task);
}
