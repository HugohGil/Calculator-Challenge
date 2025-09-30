package com.hugo.challenge.common.dto;

import java.math.BigDecimal;

public class CalculatorRequest {
    private String requestId;
    private BigDecimal a;
    private BigDecimal b;
    private String operation;

    public CalculatorRequest() {
    }

    public CalculatorRequest(String requestId, BigDecimal a, BigDecimal b, String operation) {
        this.requestId = requestId;
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRequestId() {
        return requestId;
    }

    public BigDecimal getA() {
        return a;
    }

    public BigDecimal getB() {
        return b;
    }

    public String getOperation() {
        return operation;
    }
}
