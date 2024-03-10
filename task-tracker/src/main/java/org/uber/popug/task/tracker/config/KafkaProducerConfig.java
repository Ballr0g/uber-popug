package org.uber.popug.task.tracker.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.kafka.producer.impl.KafkaTemplateTasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.impl.KafkaTemplateTasksCUDEventProducer;
import org.uber.popug.task.tracker.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.task.tracker.mapping.TasksCUDKafkaEventMapper;

import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        final var configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public TasksBusinessEventProducer tasksBusinessEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper
    ) {
        return new KafkaTemplateTasksBusinessEventProducer(kafkaTemplate, tasksBusinessKafkaEventMapper);
    }

    @Bean
    public TasksCUDEventProducer tasksCUDEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            TasksCUDKafkaEventMapper tasksCUDKafkaEventMapper
    ) {
        return new KafkaTemplateTasksCUDEventProducer(kafkaTemplate, tasksCUDKafkaEventMapper);
    }

}
