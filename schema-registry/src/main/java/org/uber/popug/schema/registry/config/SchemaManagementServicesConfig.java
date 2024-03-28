package org.uber.popug.schema.registry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.uber.popug.schema.registry.domain.schema.SchemaIdentityBuilder;
import org.uber.popug.schema.registry.domain.schema.impl.ClasspathResourceSchemaIdentityBuilder;
import org.uber.popug.schema.registry.service.SchemaLoaderService;
import org.uber.popug.schema.registry.service.SchemaRetrievalService;
import org.uber.popug.schema.registry.service.impl.SchemaRetrievalServiceImpl;
import org.uber.popug.schema.registry.service.impl.SpringResourceSchemaLoaderService;

@Configuration(proxyBeanMethods = false)
public class SchemaManagementServicesConfig {

    @Bean
    public SchemaIdentityBuilder schemaIdentityBuilder() {
        return new ClasspathResourceSchemaIdentityBuilder();
    }

    @Bean
    public SchemaLoaderService schemaLoaderService(
            SchemaIdentityBuilder schemaIdentityBuilder,
            ResourceLoader resourceLoader
    ) {
        return new SpringResourceSchemaLoaderService(schemaIdentityBuilder, resourceLoader);
    }

    @Bean
    public SchemaRetrievalService schemaRetrievalService(SchemaLoaderService schemaLoaderService) {
        return new SchemaRetrievalServiceImpl(schemaLoaderService);
    }

}
