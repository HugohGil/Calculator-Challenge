package com.hugo.challenge.common.dto;

import java.math.BigDecimal;

public class CalculatorResponse {
    private String responseId;
    private BigDecimal result;

    public CalculatorResponse() {
    }

    public CalculatorResponse(String responseId, BigDecimal result) {
        this.responseId = responseId;
        this.result = result;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public String getResponseId() {
        return responseId;
    }

    public BigDecimal getResult() {
        return result;
    }
}
