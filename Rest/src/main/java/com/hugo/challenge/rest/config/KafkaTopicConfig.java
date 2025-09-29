package com.hugo.challenge.rest.config;

import com.hugo.challenge.common.config.CommonKafkaTopicConfig;
import com.hugo.challenge.common.dto.CalculatorRequest;
import com.hugo.challenge.common.dto.CalculatorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;


@Configuration
public class KafkaTopicConfig {
    private final CommonKafkaTopicConfig commonKafka;

    public KafkaTopicConfig(CommonKafkaTopicConfig commonKafka) {
        this.commonKafka = commonKafka;
    }

    @Bean
    public KafkaTemplate<String, CalculatorRequest> requestKafkaTemplate() {
        return commonKafka.kafkaTemplate();
    }

    @Bean
    public KafkaMessageListenerContainer<String, CalculatorResponse> replyContainer() {
        ContainerProperties containerProps = new ContainerProperties("calculator-responses");
        return new KafkaMessageListenerContainer<>(
                commonKafka.consumerFactory(CalculatorResponse.class, "rest-replies"),
                containerProps
        );
    }

    @Bean
    public ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> replyingKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(requestKafkaTemplate().getProducerFactory(), replyContainer());
    }
}
