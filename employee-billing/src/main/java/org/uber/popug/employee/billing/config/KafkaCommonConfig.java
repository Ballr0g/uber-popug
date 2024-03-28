package org.uber.popug.employee.billing.config;

import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Common Kafka configuration used for both producers and consumers.
 */
@Configuration(proxyBeanMethods = false)
public class KafkaCommonConfig {

    @Bean
    public JsonSchemaFactory jsonSchemaFactory() {
        return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
    }

}
