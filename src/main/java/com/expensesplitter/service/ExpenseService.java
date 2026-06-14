package com.expensesplitter.service;

import com.expensesplitter.request.CreateExpenseRequest;
import com.expensesplitter.response.ExpenseResponse;

public interface ExpenseService {

    ExpenseResponse createExpense(
            CreateExpenseRequest request
    );
}