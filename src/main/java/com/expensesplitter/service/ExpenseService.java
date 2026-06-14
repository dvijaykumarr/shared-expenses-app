package com.expensesplitter.service;

import com.expensesplitter.request.CreateExpenseRequest;
import com.expensesplitter.response.BalanceResponse;
import com.expensesplitter.response.ExpenseResponse;
import com.expensesplitter.response.SettlementResponse;

import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(CreateExpenseRequest request);

    List<BalanceResponse> getGroupBalances(Long groupId);
    List<SettlementResponse> simplifyBalances(
            Long groupId
    );
}