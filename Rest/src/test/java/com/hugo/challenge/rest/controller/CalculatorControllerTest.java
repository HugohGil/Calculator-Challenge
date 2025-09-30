package com.hugo.challenge.rest.controller;

import com.hugo.challenge.common.dto.CalculatorRequest;
import com.hugo.challenge.common.dto.CalculatorResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class CalculatorControllerTest {
    private ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> replyingKafkaTemplate;

    private RequestReplyFuture<String, CalculatorRequest, CalculatorResponse> requestReplyFuture;

    private CalculatorController calculatorController;

    @BeforeEach
    void setup() {
        replyingKafkaTemplate = mock(ReplyingKafkaTemplate.class);
        requestReplyFuture = mock(RequestReplyFuture.class);
        calculatorController = new CalculatorController(replyingKafkaTemplate);
    }

    @Test
    void testControllerDivide() throws Exception {
        CalculatorResponse calculatorResponse = new CalculatorResponse("id-1", new BigDecimal("2"));
        ConsumerRecord<String, CalculatorResponse> consumerRecord =
                new ConsumerRecord<>("calculator-responses", 0, 0L, null, calculatorResponse);

        when(replyingKafkaTemplate.sendAndReceive(any(ProducerRecord.class))).thenReturn(requestReplyFuture);
        when(requestReplyFuture.get()).thenReturn(consumerRecord);

        ResponseEntity<BigDecimal> response = calculatorController.divide(new BigDecimal("10"), new BigDecimal("5"));

        assertEquals(new BigDecimal("2"), response.getBody());
        assertTrue(response.getHeaders().containsKey("Request-ID"));

        verify(replyingKafkaTemplate, times(1)).sendAndReceive(any(ProducerRecord.class));
    }
}
