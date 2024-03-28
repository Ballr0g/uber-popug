package org.uber.popug.task.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.domain.task.creation.TaskForCreation;
import org.uber.popug.task.tracker.exception.JiraIdFormatException;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.mapping.TasksDtoMapper;
import org.uber.popug.task.tracker.repository.TaskRepository;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskRequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskResponseDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskV2RequestDto;
import org.uber.popug.task.tracker.rest.generated.model.PostAddTaskV2ResponseDto;
import org.uber.popug.task.tracker.service.TaskAssignmentService;
import org.uber.popug.task.tracker.service.TaskAddingService;
import org.uber.popug.task.tracker.validation.TaskCreationValidator;

@RequiredArgsConstructor
public class TaskAddingServiceImpl implements TaskAddingService {

    private static final long SIMULATION_TIMEOUT_MILLIS_TO_TEMPORARILY_SLOW_DOWN_BUSINESS_EVENT = 500L;

    private final TasksDtoMapper tasksDtoMapper;
    private final TaskAssignmentService taskAssignmentService;
    private final TaskRepository taskRepository;
    private final TasksBusinessEventProducer tasksBusinessEventProducer;
    private final TasksCUDEventProducer tasksCUDEventProducer;
    private final TaskCreationValidator taskCreationValidator;

    @Override
    public PostAddTaskResponseDto addNewTask(PostAddTaskRequestDto postTasksRequestDto) {
        final var taskForCreation = tasksDtoMapper.postTaskRequestsDtoToBusiness(postTasksRequestDto);
        final var assignedTask = addNewTaskCommon(taskForCreation);

        return tasksDtoMapper.postTasksResponseDtoFromBusiness(assignedTask);
    }

    @Override
    public PostAddTaskV2ResponseDto addNewTask(PostAddTaskV2RequestDto postTasksV2RequestDto) {
        final var taskForCreation = tasksDtoMapper.postTaskRequestsDtoToBusiness(postTasksV2RequestDto);
        final var formatViolations = taskCreationValidator.validateTaskForCreationV2(taskForCreation);
        if (!formatViolations.isEmpty()) {
            throw new JiraIdFormatException(formatViolations);
        }

        final var assignedTask = addNewTaskCommon(taskForCreation);

        return tasksDtoMapper.postTasksResponseV2DtoFromBusiness(assignedTask);
    }

    @SneakyThrows
    private Task addNewTaskCommon(TaskForCreation taskForCreation) {
        final var assignedTask = taskAssignmentService.assignNewTask(taskForCreation);
        taskRepository.add(assignedTask);
        // Todo: transactional event producing.
        tasksCUDEventProducer.sendTaskCreatedReplicationEvent(assignedTask);
        // Todo: get rid of this monstrosity, currently solves receiving CUD before business in the worst possible way.
        Thread.sleep(SIMULATION_TIMEOUT_MILLIS_TO_TEMPORARILY_SLOW_DOWN_BUSINESS_EVENT);
        tasksBusinessEventProducer.sendTaskCreationEvent(assignedTask);

        return assignedTask;
    }

}
