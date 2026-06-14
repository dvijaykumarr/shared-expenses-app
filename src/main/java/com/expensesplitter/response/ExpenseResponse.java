package com.expensesplitter.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ExpenseResponse {

    private Long id;

    private String title;

    private BigDecimal amount;

    private BigDecimal normalizedAmount;

    private String currency;

    private String splitType;
}