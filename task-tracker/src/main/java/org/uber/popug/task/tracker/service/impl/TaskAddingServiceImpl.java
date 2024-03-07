package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostTasksResponseDto;
import org.uber.popug.task.tracker.service.TaskAssignmentService;
import org.uber.popug.task.tracker.service.TaskAddingService;

@RequiredArgsConstructor
public class TaskAddingServiceImpl implements TaskAddingService {

    private final TasksDtoMapper tasksDtoMapper;
    private final TaskAssignmentService taskAssignmentService;
    private final TaskRepository taskRepository;
    private final TasksCUDEventProducer tasksCUDEventProducer;

    @Override
    public PostTasksResponseDto addNewTask(PostTasksRequestDto postTasksRequestDto) {
        final var taskForCreation = tasksDtoMapper.postTaskRequestsDtoToBusiness(postTasksRequestDto);
        final var assignedTask = taskAssignmentService.assignNewTask(taskForCreation);
        taskRepository.save(assignedTask);
        tasksCUDEventProducer.sendTaskCreationEvent(assignedTask);

        return tasksDtoMapper.postTasksResponseDtoFromBusiness(assignedTask);
    }

}
