package com.expensesplitter.request;

import com.expensesplitter.enums.Currency;
import com.expensesplitter.enums.SplitType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateExpenseRequest {

    private String title;

    private String description;

    private BigDecimal amount;

    private Currency currency;

    private BigDecimal exchangeRate;

    private SplitType splitType;

    private LocalDate expenseDate;

    private Long paidBy;

    private Long groupId;

    private List<ExpenseParticipantRequest> participants;

}