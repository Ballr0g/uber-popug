package org.uber.popug.task.tracker.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCompletedEvent;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskReassignedEvent;
import org.uber.popug.task.tracker.mapping.TasksBusinessKafkaEventMapper;

import java.util.List;

@RequiredArgsConstructor
public class KafkaTemplateTasksBusinessEventProducer implements TasksBusinessEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper;

    @Value("${kafka.topics.tasks-business}")
    private String tasksBusinessKafkaTopicName;

    @Override
    public void sendTaskCreationEvent(Task task) {
        final var kafkaTaskMessage = tasksBusinessKafkaEventMapper.toTaskCreatedEventFromBusiness(task);
        final var kafkaTaskProducerRecord = kafkaTaskMessage.asProducerRecord(tasksBusinessKafkaTopicName);

        kafkaTemplate.send(kafkaTaskProducerRecord);
    }

    @Override
    @Transactional
    public void sendTaskReassignmentEvents(List<TaskReassignedEvent> taskReassignedEvents) {
        taskReassignedEvents.stream()
                .map(task -> task.asProducerRecord(tasksBusinessKafkaTopicName))
                .forEach(kafkaTemplate::send);
    }

    @Override
    public void sendTaskCompletionEvent(TaskCompletedEvent taskCompletedEvent) {
        final var kafkaTaskProducerRecord = taskCompletedEvent.asProducerRecord(tasksBusinessKafkaTopicName);

        kafkaTemplate.send(kafkaTaskProducerRecord);
    }

}
