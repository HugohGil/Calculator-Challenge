package com.hugo.challenge.rest.controller;

import com.hugo.challenge.common.dto.CalculatorRequest;
import com.hugo.challenge.common.dto.CalculatorResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> replyingKafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(CalculatorController.class);

    public CalculatorController(ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorResponse> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    private ResponseEntity<BigDecimal> sendAndReceive(BigDecimal a, BigDecimal b, String operation) throws Exception {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        try {
            CalculatorRequest request = new CalculatorRequest(requestId, a, b, operation);
            log.info("Sending Kafka request: {}", request);

            ProducerRecord<String, CalculatorRequest> record = new ProducerRecord<>("calculator-requests", request);
            RequestReplyFuture<String, CalculatorRequest, CalculatorResponse> future = replyingKafkaTemplate.sendAndReceive(record);

            CalculatorResponse response = future.get().value();
            log.info("Received Kafka response: {}", response);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Request-ID", requestId);

            return new ResponseEntity<>(response.getResult(), headers, HttpStatus.OK);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/sum")
    public ResponseEntity<BigDecimal> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b) throws Exception {
        return sendAndReceive(a, b, "sum");
    }

    @GetMapping("/subtract")
    public ResponseEntity<BigDecimal> subtract(@RequestParam BigDecimal a, @RequestParam BigDecimal b) throws Exception {
        return sendAndReceive(a, b, "subtract");
    }

    @GetMapping("/multiply")
    public ResponseEntity<BigDecimal> multiply(@RequestParam BigDecimal a, @RequestParam BigDecimal b) throws Exception {
        return sendAndReceive(a, b, "multiply");
    }

    @GetMapping("/divide")
    public ResponseEntity<BigDecimal> divide(@RequestParam BigDecimal a, @RequestParam BigDecimal b) throws Exception {
        return sendAndReceive(a, b, "divide");
    }
}
