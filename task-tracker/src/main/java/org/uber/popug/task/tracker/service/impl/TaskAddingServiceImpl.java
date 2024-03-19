package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskResponseDto;
import org.uber.popug.task.tracker.service.TaskAssignmentService;
import org.uber.popug.task.tracker.service.TaskAddingService;

@RequiredArgsConstructor
public class TaskAddingServiceImpl implements TaskAddingService {

    private static final long SIMULATION_TIMEOUT_MILLIS_TO_TEMPORARILY_SLOW_DOWN_BUSINESS_EVENT = 500L;

    private final TasksDtoMapper tasksDtoMapper;
    private final TaskAssignmentService taskAssignmentService;
    private final TaskRepository taskRepository;
    private final TasksBusinessEventProducer tasksBusinessEventProducer;
    private final TasksCUDEventProducer tasksCUDEventProducer;

    @Override
    @SneakyThrows
    public PostAddTaskResponseDto addNewTask(PostAddTaskRequestDto postTasksRequestDto) {
        final var taskForCreation = tasksDtoMapper.postTaskRequestsDtoToBusiness(postTasksRequestDto);
        final var assignedTask = taskAssignmentService.assignNewTask(taskForCreation);
        taskRepository.add(assignedTask);
        tasksCUDEventProducer.sendTaskCreatedReplicationEvent(assignedTask);
        // Todo: get rid of this monstrosity, currently solves receiving CUD before business in the worst possible way.
        Thread.sleep(SIMULATION_TIMEOUT_MILLIS_TO_TEMPORARILY_SLOW_DOWN_BUSINESS_EVENT);
        tasksBusinessEventProducer.sendTaskCreationEvent(assignedTask);

        return tasksDtoMapper.postTasksResponseDtoFromBusiness(assignedTask);
    }

}
