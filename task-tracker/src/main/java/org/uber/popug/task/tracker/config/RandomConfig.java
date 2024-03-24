package org.uber.popug.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.random.RandomGenerator;

@Configuration(proxyBeanMethods = false)
public class RandomConfig {

    @Bean
    public RandomGenerator javaRandom() {
        return new Random();
    }

}
