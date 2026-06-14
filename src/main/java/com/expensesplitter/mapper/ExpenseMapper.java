package com.expensesplitter.mapper;

import com.expensesplitter.model.Expense;
import com.expensesplitter.request.CreateExpenseRequest;
import com.expensesplitter.response.ExpenseResponse;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public Expense toEntity(CreateExpenseRequest request) {

        return Expense.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .exchangeRate(request.getExchangeRate())
                .splitType(request.getSplitType())
                .expenseDate(request.getExpenseDate())
                .build();
    }

    public ExpenseResponse toResponse(Expense expense) {

        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .normalizedAmount(expense.getNormalizedAmount())
                .currency(expense.getCurrency().name())
                .splitType(expense.getSplitType().name())
                .build();
    }
}