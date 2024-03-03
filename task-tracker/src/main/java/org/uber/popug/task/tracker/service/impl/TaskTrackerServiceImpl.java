package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;
import org.uber.popug.task.tracker.service.TaskAssignmentService;
import org.uber.popug.task.tracker.service.TaskTrackerService;

@RequiredArgsConstructor
public class TaskTrackerServiceImpl implements TaskTrackerService {
    private final TasksDtoMapper tasksDtoMapper;
    private final TaskAssignmentService taskAssignmentService;
    @Override
    public PostTasksResponseDto addNewTask(PostTasksRequestDto postTasksRequestDto) {
        final var taskForCreation = tasksDtoMapper.postTaskRequestsDtoToBusiness(postTasksRequestDto);
        final var assignedTask = taskAssignmentService.assignNewTask(taskForCreation);

        return tasksDtoMapper.postTasksResponseDtoFromBusiness(assignedTask);
    }
}
