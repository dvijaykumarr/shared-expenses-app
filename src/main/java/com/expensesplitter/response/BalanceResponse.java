package com.expensesplitter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BalanceResponse {

    private Long userId;

    private String name;

    private BigDecimal balance;
}