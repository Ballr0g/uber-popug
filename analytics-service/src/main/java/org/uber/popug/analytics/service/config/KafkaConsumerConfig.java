package org.uber.popug.analytics.service.config;

import com.networknt.schema.JsonSchemaFactory;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.uber.popug.analytics.service.kafka.common.JsonSchemaRegistry;
import org.uber.popug.analytics.service.kafka.common.JsonSchemaWithClass;
import org.uber.popug.analytics.service.kafka.common.NamedJsonSchema;
import org.uber.popug.analytics.service.kafka.common.impl.CommonJsonSchemaRegistry;
import org.uber.popug.analytics.service.kafka.consumer.deserializer.BillingOperationsCUDJsonSchemaDeserializer;
import org.uber.popug.analytics.service.kafka.generated.dto.BillingOperationCreatedEventV1;

import java.util.List;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
public class KafkaConsumerConfig {

    // Todo: refactor using Spring properties.

    @Value("${kafka.listener.billing-operations-stream.bootstrap-servers}")
    private List<String> billingOperationsStreamBootstrapServers;

    @Value("${kafka.downloaded-schemas.billing-operation-created-replication-event.v1}")
    private Resource billingOperationCreatedEventV1Schema;

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
                        "billing.operation.entity.created",
                        Map.ofEntries(
                                Map.entry(1, new JsonSchemaWithClass<>(
                                        jsonSchemaFactory.getSchema(billingOperationCreatedEventV1Schema.getInputStream()),
                                        BillingOperationCreatedEventV1.class
                                ))
                        )
                )
        );

        return new CommonJsonSchemaRegistry<>(supportedSchemas);
    }

    @Bean
    public JsonDeserializer<Object> billingOperationsCUDJsonSchemaDeserializer(
            JsonSchemaRegistry<Object> tasksCUDJsonSchemaRegistry
    ) {
        return new BillingOperationsCUDJsonSchemaDeserializer(tasksCUDJsonSchemaRegistry);
    }

    @Bean
    public ConsumerFactory<String, Object> billingOperationsCUDConsumerFactory(
            StringDeserializer stringDeserializer,
            JsonDeserializer<Object> billingOperationsCUDJsonSchemaDeserializer
    ) {
        final var taskCUDStreamConsumerProps = Map.<String, Object>ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, billingOperationsStreamBootstrapServers)
        );

        return new DefaultKafkaConsumerFactory<>(
                taskCUDStreamConsumerProps,
                stringDeserializer,
                billingOperationsCUDJsonSchemaDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> billingOperationsCUDListenerContainerFactory(
            ConsumerFactory<String, Object> billingOperationsCUDConsumerFactory
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(billingOperationsCUDConsumerFactory);
        return factory;
    }

}
