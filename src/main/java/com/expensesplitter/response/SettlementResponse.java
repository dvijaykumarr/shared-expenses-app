package com.expensesplitter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SettlementResponse {

    private String from;

    private String to;

    private BigDecimal amount;
}