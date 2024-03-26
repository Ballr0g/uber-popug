package org.uber.popug.task.tracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.uber.popug.task.tracker.kafka.producer.TasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.TasksCUDEventProducer;
import org.uber.popug.task.tracker.kafka.producer.event.cud.TaskCreatedReplicationEventFactory;
import org.uber.popug.task.tracker.kafka.producer.event.cud.impl.TaskCreatedReplicationEventFactoryImpl;
import org.uber.popug.task.tracker.kafka.producer.impl.KafkaTemplateTasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.impl.KafkaTemplateTasksCUDEventProducer;
import org.uber.popug.task.tracker.mapping.TasksBusinessKafkaEventMapper;

import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.downloaded-schemas.task-created-replication-event.v1}")
    private Resource taskCreatedReplicationEventV1Schema;

    @Bean
    public StringSerializer stringSerializer() {
        return new StringSerializer();
    }

    @Bean
    public JsonSerializer<Object> jsonSerializer(
            ObjectMapper objectMapper
    ) {
        return new JsonSerializer<>(objectMapper);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory(
            StringSerializer stringSerializer,
            JsonSerializer<Object> jsonSerializer
    ) {
        final var configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return new DefaultKafkaProducerFactory<>(configProps, stringSerializer, jsonSerializer);
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
    public JsonSchemaFactory jsonSchemaFactory() {
        return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
    }

    @Bean
    @SneakyThrows
    public JsonSchema taskCreatedReplicationEventV1JsonSchema(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        return jsonSchemaFactory.getSchema(taskCreatedReplicationEventV1Schema.getInputStream());
    }

    @Bean
    public TaskCreatedReplicationEventFactory taskCreatedReplicationEventFactory(
            ObjectMapper objectMapper,
            JsonSchema taskCreatedReplicationEventV1JsonSchema
    ) {
        return new TaskCreatedReplicationEventFactoryImpl(objectMapper, taskCreatedReplicationEventV1JsonSchema);
    }

    @Bean
    public TasksCUDEventProducer tasksCUDEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            TaskCreatedReplicationEventFactory taskCreatedReplicationEventFactory
    ) {
        return new KafkaTemplateTasksCUDEventProducer(
                kafkaTemplate,
                taskCreatedReplicationEventFactory
        );
    }

}
