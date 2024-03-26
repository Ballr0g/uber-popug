package org.uber.popug.employee.billing.config;

import com.networknt.schema.JsonSchemaFactory;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.uber.popug.employee.billing.kafka.JsonSchemaRegistry;
import org.uber.popug.employee.billing.kafka.TasksBusinessWorkflowJsonSchemaDeserializer;
import org.uber.popug.employee.billing.kafka.TasksCUDJsonSchemaDeserializer;
import org.uber.popug.employee.billing.kafka.consumer.JsonSchemaWithClass;
import org.uber.popug.employee.billing.kafka.consumer.NamedJsonSchema;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV1;
import org.uber.popug.employee.billing.kafka.impl.CommonJsonSchemaRegistry;

import java.util.List;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
public class KafkaConsumerConfig {

    // Todo: refactor using Spring properties.

    @Value("${kafka.listener.task-lifecycle-stream.bootstrap-servers}")
    private List<String> taskLifecycleStreamBootstrapServers;

    @Value("${kafka.listener.task-workflow-actions.bootstrap-servers}")
    private List<String> taskWorkflowActionsBootstrapServers;

    @Value("${kafka.downloaded-schemas.task-created-replication-event.v1}")
    private Resource taskReplicationEventV1Schema;

    @Bean
    public StringDeserializer stringDeserializer() {
        return new StringDeserializer();
    }

    @Bean
    @SneakyThrows
    public JsonSchemaRegistry<Object> tasksCUDJsonSchemaRegistry(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        final var supportedSchemas = List.<NamedJsonSchema<Object>>of(
                new NamedJsonSchema<>(
                        "task.entity.created",
                        Map.ofEntries(
                                Map.entry(1, new JsonSchemaWithClass<>(
                                        jsonSchemaFactory.getSchema(taskReplicationEventV1Schema.getInputStream()),
                                        TaskCreatedReplicationEventV1.class
                                ))
                        )
                )
        );

        return new CommonJsonSchemaRegistry<>(supportedSchemas);
    }

    @Bean
    public JsonDeserializer<Object> tasksCUDJsonSchemaDeserializer(
            JsonSchemaRegistry<Object> tasksCUDJsonSchemaRegistry
    ) {
        return new TasksCUDJsonSchemaDeserializer(tasksCUDJsonSchemaRegistry);
    }

    @Bean("tasksCUDConsumerFactory")
    public ConsumerFactory<String, Object> tasksCUDConsumerFactory(
            StringDeserializer stringDeserializer,
            JsonDeserializer<Object> tasksCUDJsonSchemaDeserializer
    ) {
        final var taskCUDStreamConsumerProps = Map.<String, Object>ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, taskLifecycleStreamBootstrapServers)
        );

        return new DefaultKafkaConsumerFactory<>(
                taskCUDStreamConsumerProps,
                stringDeserializer,
                tasksCUDJsonSchemaDeserializer
        );
    }

    @Bean("tasksCUDListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    tasksCUDListenerContainerFactory(
            @Qualifier("tasksCUDConsumerFactory") ConsumerFactory<String, Object> consumerFactory
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    @SneakyThrows
    public JsonSchemaRegistry<Object> tasksBusinessJsonSchemaRegistry(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        final var supportedSchemas = List.<NamedJsonSchema<Object>>of();

        return new CommonJsonSchemaRegistry<>(supportedSchemas);
    }

    @Bean
    public JsonDeserializer<Object> tasksBusinessWorkflowJsonSchemaDeserializer(
            JsonSchemaRegistry<Object> tasksBusinessJsonSchemaRegistry
    ) {
        return new TasksBusinessWorkflowJsonSchemaDeserializer(tasksBusinessJsonSchemaRegistry);
    }

    @Bean("tasksBusinessWorkflowConsumerFactory")
    public ConsumerFactory<String, Object> tasksBusinessWorkflowConsumerFactory(
            StringDeserializer stringDeserializer,
            JsonDeserializer<Object> tasksBusinessWorkflowJsonSchemaDeserializer
    ) {
        final var taskBusinessConsumerProps = Map.<String, Object>ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, taskWorkflowActionsBootstrapServers)
        );

        return new DefaultKafkaConsumerFactory<>(
                taskBusinessConsumerProps,
                stringDeserializer,
                tasksBusinessWorkflowJsonSchemaDeserializer
        );
    }

    @Bean("tasksBusinessWorkflowListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    tasksBusinessWorkflowListenerContainerFactory(
            @Qualifier("tasksBusinessWorkflowConsumerFactory") ConsumerFactory<String, Object> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
