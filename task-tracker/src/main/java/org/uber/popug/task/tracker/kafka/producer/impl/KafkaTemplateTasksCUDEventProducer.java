package org.uber.popug.task.tracker.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.mapping.TasksKafkaEventMapper;

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

}
