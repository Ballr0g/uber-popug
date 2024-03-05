package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.TaskForCreation;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;

@Mapper
public interface TasksDtoMapper {

    @Mapping(source = "description", target = "taskDescription")
    TaskForCreation postTaskRequestsDtoToBusiness(PostTasksRequestDto postTasksRequestDto);

    @Mapping(source = "publicTaskId", target = "taskId")
    @Mapping(source = "assignee.assigneeId", target = "taskAssigneeId")
    PostTasksResponseDto postTasksResponseDtoFromBusiness(Task task);

}
