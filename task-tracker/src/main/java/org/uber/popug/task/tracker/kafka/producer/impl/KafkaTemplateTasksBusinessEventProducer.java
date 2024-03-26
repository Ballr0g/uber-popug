package org.uber.popug.task.tracker.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.task.tracker.domain.task.Task;
import org.uber.popug.task.tracker.entity.composite.ReassignedTaskEntity;
import org.uber.popug.task.tracker.entity.task.TaskEntity;
import org.uber.popug.task.tracker.entity.user.UserEntity;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCompletedEventFactory;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCreatedEventFactory;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskReassignedEventFactory;

import java.util.List;

@RequiredArgsConstructor
public class KafkaTemplateTasksBusinessEventProducer implements TasksBusinessEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TaskCreatedEventFactory taskCreatedEventFactory;
    private final TaskReassignedEventFactory taskReassignedEventFactory;
    private final TaskCompletedEventFactory taskCompletedEventFactory;

    @Value("${kafka.topics.tasks-business}")
    private String tasksBusinessKafkaTopicName;

    @Override
    public void sendTaskCreationEvent(Task task) {
        final var taskCreatedProducerRecord
                = taskCreatedEventFactory.createTaskCreatedEventV1(task, tasksBusinessKafkaTopicName);

        kafkaTemplate.send(taskCreatedProducerRecord);
    }

    @Override
    @Transactional
    public void sendTaskReassignmentEventsTransactional(List<ReassignedTaskEntity> tasksForReassignment) {
        tasksForReassignment.stream()
                .map(task -> taskReassignedEventFactory.createTaskReassignedEventV1(task, tasksBusinessKafkaTopicName))
                .forEach(kafkaTemplate::send);
    }

    @Override
    public void sendTaskCompletionEvent(TaskEntity task, UserEntity assignee) {
        final var taskCreatedProducerRecord
                = taskCompletedEventFactory.createTaskCompletedEventV1(task, assignee, tasksBusinessKafkaTopicName);

        kafkaTemplate.send(taskCreatedProducerRecord);
    }

}
