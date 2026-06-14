package com.expensesplitter.service.impl;

import com.expensesplitter.mapper.ExpenseMapper;
import com.expensesplitter.model.*;
import com.expensesplitter.repository.*;
import com.expensesplitter.request.CreateExpenseRequest;
import com.expensesplitter.response.ExpenseResponse;
import com.expensesplitter.service.ExpenseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    @Transactional
    public ExpenseResponse createExpense(
            CreateExpenseRequest request
    ) {

        User paidBy = userRepository.findById(request.getPaidBy())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        Expense expense = expenseMapper.toEntity(request);

        expense.setPaidBy(paidBy);

        expense.setGroup(group);

        BigDecimal normalizedAmount =
                request.getAmount()
                        .multiply(request.getExchangeRate());

        expense.setNormalizedAmount(normalizedAmount);

        Expense savedExpense =
                expenseRepository.save(expense);

        int participantCount =
                request.getParticipantIds().size();

        BigDecimal splitAmount =
                normalizedAmount.divide(
                        BigDecimal.valueOf(participantCount),
                        2,
                        RoundingMode.HALF_UP
                );

        for (Long participantId : request.getParticipantIds()) {

            User participant =
                    userRepository.findById(participantId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Participant not found"
                                    ));

            ExpenseParticipant expenseParticipant =
                    ExpenseParticipant.builder()
                            .expense(savedExpense)
                            .user(participant)
                            .shareAmount(splitAmount)
                            .build();

            participantRepository.save(expenseParticipant);
        }

        return expenseMapper.toResponse(savedExpense);
    }
}