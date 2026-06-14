package com.expensesplitter.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExpenseParticipantRequest {

    private Long userId;

    private BigDecimal shareAmount;

    private BigDecimal percentage;
}