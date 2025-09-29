package com.hugo.challenge.calculator.consumer;

import com.hugo.challenge.calculator.service.CalculatorService;
import com.hugo.challenge.common.dto.CalculatorRequest;
import com.hugo.challenge.common.dto.CalculatorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculatorConsumer {
    private final CalculatorService calculatorService;
    private static final Logger log = LoggerFactory.getLogger(CalculatorConsumer.class);

    public CalculatorConsumer(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @KafkaListener(topics = "calculator-requests", groupId = "calculator")
    @SendTo
    public CalculatorResponse consume(CalculatorRequest request) {
        MDC.put("requestId", request.getRequestId());
        try {
            log.info("Received Kafka request: {}", request);

            BigDecimal result = switch (request.getOperation()) {
                case "sum" -> calculatorService.sum(request.getA(), request.getB());
                case "subtract" -> calculatorService.subtract(request.getA(), request.getB());
                case "multiply" -> calculatorService.multiply(request.getA(), request.getB());
                case "divide" -> calculatorService.divide(request.getA(), request.getB());
                default -> {
                    log.error("Invalid operation: {}", request.getOperation());
                    throw new IllegalArgumentException("Invalid operation: " + request.getOperation());
                }
            };

            CalculatorResponse response = new CalculatorResponse(request.getRequestId(), result);
            log.info("Sending Kafka response: {}", response);

            return response;
        } finally {
            MDC.clear();
        }
    }
}