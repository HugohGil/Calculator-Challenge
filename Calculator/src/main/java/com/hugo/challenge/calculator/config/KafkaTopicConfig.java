package com.hugo.challenge.calculator.config;

import com.hugo.challenge.common.config.CommonKafkaTopicConfig;
import com.hugo.challenge.common.dto.CalculatorRequest;
import com.hugo.challenge.common.dto.CalculatorResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

@Configuration
public class KafkaTopicConfig {
    private final CommonKafkaTopicConfig common;

    public KafkaTopicConfig(CommonKafkaTopicConfig common) {
        this.common = common;
    }

    @Bean
    public KafkaTemplate<String, CalculatorResponse> kafkaTemplate() {
        return common.kafkaTemplate();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CalculatorRequest> kafkaListenerContainerFactory(
            KafkaTemplate<String, CalculatorResponse> kafkaTemplate) {
        return common.listenerContainerFactory(
                common.consumerFactory(CalculatorRequest.class, "calculator"),
                kafkaTemplate
        );
    }
}
