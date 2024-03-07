package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.creation.TaskForCreation;
import org.uber.popug.task.tracker.rest.generated.model.GetTasksByUserTaskDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;

import java.util.List;

@Mapper
public interface TasksDtoMapper {

    @Mapping(source = "description", target = "taskDescription")
    TaskForCreation postTaskRequestsDtoToBusiness(PostTasksRequestDto postTasksRequestDto);

    @Mapping(source = "publicTaskId", target = "taskId")
    @Mapping(source = "assignee.publicAssigneeId", target = "taskAssigneeId")
    PostTasksResponseDto postTasksResponseDtoFromBusiness(Task task);

    @Mapping(source = "publicTaskId", target = "taskId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    GetTasksByUserTaskDto getTasksByUserResponseDtoFromBusiness(Task task);

    List<GetTasksByUserTaskDto> getTasksByUserResponseDtoListFromBusiness(List<Task> tasks);
}
