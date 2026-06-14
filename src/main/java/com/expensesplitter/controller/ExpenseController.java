package com.expensesplitter.controller;

import com.expensesplitter.request.CreateExpenseRequest;
import com.expensesplitter.response.ExpenseResponse;
import com.expensesplitter.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}