package org.uber.popug.employee.billing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
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
import org.uber.popug.employee.billing.kafka.event.cud.BillingOperationCreatedReplicationEventFactory;
import org.uber.popug.employee.billing.kafka.event.cud.impl.BillingOperationCreatedReplicationEventFactoryImpl;
import org.uber.popug.employee.billing.kafka.producer.BillingOperationsCUDEventProducer;
import org.uber.popug.employee.billing.kafka.producer.impl.kafkaTemplateBillingOperationsCUDEventProducer;

import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.downloaded-schemas.billing-operation-created-replication-event.v1}")
    private Resource billingOperationCreatedEventV1Schema;

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
    public JsonSchema billingOperationCreatedEventV1JsonSchema(
            JsonSchemaFactory jsonSchemaFactory
    ) {
        return jsonSchemaFactory.getSchema(billingOperationCreatedEventV1Schema.getInputStream());
    }

    @Bean
    public BillingOperationCreatedReplicationEventFactory billingOperationCreatedReplicationEventFactory(
            ObjectMapper objectMapper,
            JsonSchema billingOperationCreatedReplicationEventV1JsonSchema
    ) {
        return new BillingOperationCreatedReplicationEventFactoryImpl(
                objectMapper,
                billingOperationCreatedReplicationEventV1JsonSchema
        );
    }

    @Bean
    public BillingOperationsCUDEventProducer billingOperationsCUDEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            BillingOperationCreatedReplicationEventFactory billingOperationCreatedReplicationEventFactory
    ) {
        return new kafkaTemplateBillingOperationsCUDEventProducer(
                kafkaTemplate,
                billingOperationCreatedReplicationEventFactory
        );
    }

}
