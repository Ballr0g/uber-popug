package org.uber.popug.task.tracker.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.creation.TaskForCreation;
import org.uber.popug.task.tracker.rest.generated.model.GetTasksByUserTaskDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskResponseDto;

import java.util.List;

@Mapper
public interface TasksDtoMapper {

    @Mapping(source = "taskDescription", target = "description")
    TaskForCreation postTaskRequestsDtoToBusiness(PostAddTaskRequestDto postTasksRequestDto);

    @Mapping(source = "publicId", target = "taskId")
    @Mapping(source = "assignee.extPublicId", target = "taskAssigneeId")
    PostAddTaskResponseDto postTasksResponseDtoFromBusiness(Task task);

    @Mapping(source = "publicId", target = "taskId")
    @Mapping(source = "description", target = "taskDescription")
    GetTasksByUserTaskDto getTasksByUserResponseDtoFromBusiness(Task task);

    List<GetTasksByUserTaskDto> getTasksByUserResponseDtoListFromBusiness(List<Task> tasks);
}
