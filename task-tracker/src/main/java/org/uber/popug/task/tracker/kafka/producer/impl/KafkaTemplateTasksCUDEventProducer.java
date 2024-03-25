package org.uber.popug.task.tracker.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.kafka.producer.event.cud.TaskCreatedReplicationEventFactory;

@RequiredArgsConstructor
public class KafkaTemplateTasksCUDEventProducer implements TasksCUDEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TaskCreatedReplicationEventFactory taskCreatedReplicationEventFactory;

    @Value("${kafka.topics.tasks-cud}")
    private String tasksCUDKafkaTopicName;

    @Override
    public void sendTaskCreatedReplicationEvent(Task task) {
        final var taskCreatedProducerRecord
                = taskCreatedReplicationEventFactory.createdReplicationEventV1(task, tasksCUDKafkaTopicName);

        kafkaTemplate.send(taskCreatedProducerRecord);
    }

}
