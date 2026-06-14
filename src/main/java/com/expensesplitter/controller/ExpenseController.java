package com.expensesplitter.controller;

import com.expensesplitter.request.CreateExpenseRequest;
import com.expensesplitter.response.BalanceResponse;
import com.expensesplitter.response.ExpenseResponse;
import com.expensesplitter.response.SettlementResponse;
import com.expensesplitter.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse createExpense(
            @RequestBody CreateExpenseRequest request
    ) {

        return expenseService.createExpense(request);
    }

    @GetMapping("/groups/{groupId}/balances")
    public List<BalanceResponse> getGroupBalances(
            @PathVariable Long groupId
    ) {

        return expenseService.getGroupBalances(groupId);
    }

    @GetMapping("/groups/{groupId}/settlements")
    public List<SettlementResponse> simplifyBalances(
            @PathVariable Long groupId
    ) {

        return expenseService.simplifyBalances(groupId);
    }
}