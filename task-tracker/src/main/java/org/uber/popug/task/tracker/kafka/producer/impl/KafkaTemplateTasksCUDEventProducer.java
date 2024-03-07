package org.uber.popug.task.tracker.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.kafka.producer.dto.TaskReassignedEvent;
import org.uber.popug.task.tracker.mapping.TasksKafkaEventMapper;

import java.util.List;

@RequiredArgsConstructor
public class KafkaTemplateTasksCUDEventProducer implements TasksCUDEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TasksKafkaEventMapper tasksKafkaEventMapper;

    @Value("${kafka.topic}")
    private String tasksLifecycleKafkaTopicName;

    @Override
    public void sendTaskCreationEvent(Task task) {
        final var kafkaTaskMessage = tasksKafkaEventMapper.toTaskCreatedEventFromBusiness(task);
        final var kafkaTaskProducerRecord = kafkaTaskMessage.asProducerRecord(tasksLifecycleKafkaTopicName);

        kafkaTemplate.send(kafkaTaskProducerRecord);
    }

    @Override
    @Transactional
    public void sendTaskReassignmentEvents(List<TaskReassignedEvent> taskReassignedEvents) {
        taskReassignedEvents.stream()
                .map(task -> task.asProducerRecord(tasksLifecycleKafkaTopicName))
                .forEach(kafkaTemplate::send);
    }

}
