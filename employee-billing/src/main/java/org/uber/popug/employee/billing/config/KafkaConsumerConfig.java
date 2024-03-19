package org.uber.popug.employee.billing.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.uber.popug.employee.billing.kafka.TasksBusinessWorkflowJsonDeserializer;
import org.uber.popug.employee.billing.kafka.TasksCUDJsonDeserializer;

import java.util.List;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
public class KafkaConsumerConfig {

    @Value("${kafka.listener.task-lifecycle-stream.bootstrap-servers}")
    private List<String> taskLifecycleStreamBootstrapServers;

    @Value("${kafka.listener.task-workflow-actions.bootstrap-servers}")
    private List<String> taskWorkflowActionsBootstrapServers;

    @Bean("tasksCUDConsumerFactory")
    public ConsumerFactory<String, Object> tasksCUDConsumerFactory() {
        final var taskCUDStreamConsumerProps = Map.<String, Object>ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, taskLifecycleStreamBootstrapServers)
        );

        return new DefaultKafkaConsumerFactory<>(
                taskCUDStreamConsumerProps,
                StringDeserializer::new,
                TasksCUDJsonDeserializer::new
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

    @Bean("tasksBusinessWorkflowConsumerFactory")
    public ConsumerFactory<String, Object> tasksBusinessWorkflowConsumerFactory() {
        final var taskBusinessConsumerProps = Map.<String, Object>ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, taskWorkflowActionsBootstrapServers)
        );

        return new DefaultKafkaConsumerFactory<>(
                taskBusinessConsumerProps,
                StringDeserializer::new,
                TasksBusinessWorkflowJsonDeserializer::new
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
