package com.expensesplitter.repository;

import com.expensesplitter.model.ExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseParticipantRepository
        extends JpaRepository<ExpenseParticipant, Long> {
}