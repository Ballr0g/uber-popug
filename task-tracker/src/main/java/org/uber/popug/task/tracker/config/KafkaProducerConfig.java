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
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCompletedEventFactory;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskCreatedEventFactory;
import org.uber.popug.task.tracker.kafka.producer.event.business.TaskReassignedEventFactory;
import org.uber.popug.task.tracker.kafka.producer.event.business.impl.TaskCompletedEventFactoryImpl;
import org.uber.popug.task.tracker.kafka.producer.event.business.impl.TaskCreatedEventFactoryImpl;
import org.uber.popug.task.tracker.kafka.producer.event.business.impl.TaskReassignedEventFactoryImpl;
import org.uber.popug.task.tracker.kafka.producer.event.cud.TaskCreatedReplicationEventFactory;
import org.uber.popug.task.tracker.kafka.producer.event.cud.impl.TaskCreatedReplicationEventFactoryImpl;
import org.uber.popug.task.tracker.kafka.producer.impl.KafkaTemplateTasksBusinessEventProducer;
import org.uber.popug.task.tracker.kafka.producer.impl.KafkaTemplateTasksCUDEventProducer;

import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.downloaded-schemas.task-created-replication-event.v1}")
    private Resource taskCreatedReplicationEventV1Schema;

    @Value("${kafka.downloaded-schemas.task-created-replication-event.v2}")
    private Resource taskCreatedReplicationEventV2Schema;

    @Value("${kafka.downloaded-schemas.task-created-event.v1}")
    private Resource taskCreatedEventV1Schema;

    @Value("${kafka.downloaded-schemas.task-reassigned-event.v1}")
    private Resource taskReassignedEventV1Schema;

    @Value("${kafka.downloaded-schemas.task-completed-event.v1}")
    private Resource taskCompletedEventV1Schema;

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
    @SneakyThrows
    public JsonSchema taskCreatedEventV1JsonSchema(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        return jsonSchemaFactory.getSchema(taskCreatedEventV1Schema.getInputStream());
    }

    @Bean
    public TaskCreatedEventFactory taskCreatedEventFactory(
            ObjectMapper objectMapper,
            JsonSchema taskCreatedEventV1JsonSchema
    ) {
        return new TaskCreatedEventFactoryImpl(objectMapper, taskCreatedEventV1JsonSchema);
    }

    @Bean
    @SneakyThrows
    public JsonSchema taskReassignedEventV1JsonSchema(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        return jsonSchemaFactory.getSchema(taskReassignedEventV1Schema.getInputStream());
    }

    @Bean
    public TaskReassignedEventFactory taskReassignedEventFactory(
            ObjectMapper objectMapper,
            JsonSchema taskReassignedEventV1JsonSchema
    ) {
        return new TaskReassignedEventFactoryImpl(objectMapper, taskReassignedEventV1JsonSchema);
    }

    @Bean
    @SneakyThrows
    public JsonSchema taskCompletedEventV1JsonSchema(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        return jsonSchemaFactory.getSchema(taskCompletedEventV1Schema.getInputStream());
    }

    @Bean
    public TaskCompletedEventFactory taskCompletedEventFactory(
            ObjectMapper objectMapper,
            JsonSchema taskCompletedEventV1JsonSchema
    ) {
        return new TaskCompletedEventFactoryImpl(objectMapper, taskCompletedEventV1JsonSchema);
    }

    @Bean
    public TasksBusinessEventProducer tasksBusinessEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            TaskCreatedEventFactory taskCreatedEventFactory,
            TaskReassignedEventFactory taskReassignedEventFactory,
            TaskCompletedEventFactory taskCompletedEventFactory
    ) {
        return new KafkaTemplateTasksBusinessEventProducer(
                kafkaTemplate,
                taskCreatedEventFactory,
                taskReassignedEventFactory,
                taskCompletedEventFactory
        );
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
    @SneakyThrows
    public JsonSchema taskCreatedReplicationEventV2JsonSchema(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        return jsonSchemaFactory.getSchema(taskCreatedReplicationEventV2Schema.getInputStream());
    }

    @Bean
    public TaskCreatedReplicationEventFactory taskCreatedReplicationEventFactory(
            ObjectMapper objectMapper,
            JsonSchema taskCreatedReplicationEventV1JsonSchema,
            JsonSchema taskCreatedReplicationEventV2JsonSchema
    ) {
        return new TaskCreatedReplicationEventFactoryImpl(
                objectMapper,
                taskCreatedReplicationEventV1JsonSchema,
                taskCreatedReplicationEventV2JsonSchema
        );
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
