package com.hugo.challenge.calculator.consumer;

import com.hugo.challenge.calculator.service.CalculatorService;
import com.hugo.challenge.common.dto.CalculatorRequest;
import com.hugo.challenge.common.dto.CalculatorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CalculatorConsumerTest {
    private CalculatorService calculatorService;
    private CalculatorConsumer calculatorConsumer;

    @BeforeEach
    void setUp() {
        calculatorService = mock(CalculatorService.class);
        calculatorConsumer = new CalculatorConsumer(calculatorService);
    }

    @Test
    void testConsumeSum() {
        when(calculatorService.sum(new BigDecimal("5"), new BigDecimal("10"))).thenReturn(new BigDecimal("15"));

        CalculatorRequest request = new CalculatorRequest("id-1", new BigDecimal("5"), new BigDecimal("10"), "sum");
        CalculatorResponse response = calculatorConsumer.consume(request);

        assertNotNull(response);
        assertEquals("id-1", response.getResponseId());
        assertEquals(new BigDecimal("15"), response.getResult());

        verify(calculatorService).sum(new BigDecimal("5"), new BigDecimal("10"));
    }
}
