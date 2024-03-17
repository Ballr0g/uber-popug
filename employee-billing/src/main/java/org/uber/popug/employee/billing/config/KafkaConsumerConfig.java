package org.uber.popug.employee.billing.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.uber.popug.employee.billing.kafka.CustomTasksCUDJsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
public class KafkaConsumerConfig {

    @Value("${kafka.listener.task-lifecycle-stream.bootstrap-servers}")
    private List<String> taskLifecycleStreamBootstrapServers;

    @Bean
    public ConsumerFactory<String, Object> tasksCUDConsumerFactory() {
        Map<String, Object> taskCUDStreamConsumerProps = new HashMap<>();
        taskCUDStreamConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, taskLifecycleStreamBootstrapServers);


        return new DefaultKafkaConsumerFactory<>(
                taskCUDStreamConsumerProps,
                StringDeserializer::new,
                CustomTasksCUDJsonDeserializer::new
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    tasksCUDListenerContainerFactory(
            ConsumerFactory<String, Object> tasksCUDConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(tasksCUDConsumerFactory);
        return factory;
    }

}
