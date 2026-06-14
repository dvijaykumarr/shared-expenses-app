package com.expensesplitter.repository;

import com.expensesplitter.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {
}