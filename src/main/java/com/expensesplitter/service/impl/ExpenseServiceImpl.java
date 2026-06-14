package com.expensesplitter.service.impl;

import com.expensesplitter.mapper.ExpenseMapper;
import com.expensesplitter.model.*;
import com.expensesplitter.repository.*;
import com.expensesplitter.request.CreateExpenseRequest;
import com.expensesplitter.request.ExpenseParticipantRequest;
import com.expensesplitter.response.BalanceResponse;
import com.expensesplitter.response.ExpenseResponse;
import com.expensesplitter.response.SettlementResponse;
import com.expensesplitter.service.ExpenseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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

        switch (request.getSplitType()) {

            case EQUAL -> handleEqualSplit(
                    request,
                    savedExpense,
                    normalizedAmount
            );

            case EXACT -> handleExactSplit(
                    request,
                    savedExpense
            );

            case PERCENTAGE -> handlePercentageSplit(
                    request,
                    savedExpense,
                    normalizedAmount
            );
        }

        return expenseMapper.toResponse(savedExpense);
    }

    private void handlePercentageSplit(
            CreateExpenseRequest request,
            Expense expense,
            BigDecimal normalizedAmount
    ) {

        BigDecimal totalPercentage =
                BigDecimal.ZERO;

        for (ExpenseParticipantRequest participantRequest
                : request.getParticipants()) {

            totalPercentage =
                    totalPercentage.add(
                            participantRequest.getPercentage()
                    );
        }

        if (totalPercentage.compareTo(
                BigDecimal.valueOf(100)
        ) != 0) {

            throw new RuntimeException(
                    "Total percentage must equal 100"
            );
        }

        for (ExpenseParticipantRequest participantRequest
                : request.getParticipants()) {

            User participant =
                    userRepository.findById(
                                    participantRequest.getUserId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Participant not found"
                                    ));

            BigDecimal shareAmount =
                    normalizedAmount.multiply(
                            participantRequest.getPercentage()
                                    .divide(
                                            BigDecimal.valueOf(100),
                                            2,
                                            RoundingMode.HALF_UP
                                    )
                    );

            ExpenseParticipant expenseParticipant =
                    ExpenseParticipant.builder()
                            .expense(expense)
                            .user(participant)
                            .shareAmount(shareAmount)
                            .percentage(
                                    participantRequest.getPercentage()
                            )
                            .build();

            participantRepository.save(expenseParticipant);
        }
    }

    private void handleExactSplit(
            CreateExpenseRequest request,
            Expense expense
    ) {

        BigDecimal total =
                BigDecimal.ZERO;

        for (ExpenseParticipantRequest participantRequest
                : request.getParticipants()) {

            total = total.add(
                    participantRequest.getShareAmount()
            );
        }

        if (total.compareTo(
                expense.getNormalizedAmount()
        ) != 0) {

            throw new RuntimeException(
                    "Exact split total does not match expense amount"
            );
        }

        for (ExpenseParticipantRequest participantRequest
                : request.getParticipants()) {

            User participant =
                    userRepository.findById(
                                    participantRequest.getUserId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Participant not found"
                                    ));

            ExpenseParticipant expenseParticipant =
                    ExpenseParticipant.builder()
                            .expense(expense)
                            .user(participant)
                            .shareAmount(
                                    participantRequest.getShareAmount()
                            )
                            .build();

            participantRepository.save(expenseParticipant);
        }
    }

    private void handleEqualSplit(
            CreateExpenseRequest request,
            Expense expense,
            BigDecimal normalizedAmount
    ) {

        int participantCount =
                request.getParticipants().size();

        BigDecimal splitAmount =
                normalizedAmount.divide(
                        BigDecimal.valueOf(participantCount),
                        2,
                        RoundingMode.HALF_UP
                );

        for (ExpenseParticipantRequest participantRequest
                : request.getParticipants()) {

            User participant =
                    userRepository.findById(
                                    participantRequest.getUserId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Participant not found"
                                    ));

            ExpenseParticipant expenseParticipant =
                    ExpenseParticipant.builder()
                            .expense(expense)
                            .user(participant)
                            .shareAmount(splitAmount)
                            .build();

            participantRepository.save(expenseParticipant);
        }
    }

    @Override
    public List<BalanceResponse> getGroupBalances(
            Long groupId
    ) {

        List<Expense> expenses =
                expenseRepository.findByGroupId(groupId);

        Map<Long, BigDecimal> balances =
                new HashMap<>();

        Map<Long, String> userNames =
                new HashMap<>();

        for (Expense expense : expenses) {

            Long paidById =
                    expense.getPaidBy().getId();

            balances.put(
                    paidById,
                    balances.getOrDefault(
                            paidById,
                            BigDecimal.ZERO
                    ).add(expense.getNormalizedAmount())
            );

            userNames.put(
                    paidById,
                    expense.getPaidBy().getName()
            );

            List<ExpenseParticipant> participants =
                    participantRepository.findByExpenseId(
                            expense.getId()
                    );

            for (ExpenseParticipant participant : participants) {

                Long userId =
                        participant.getUser().getId();

                balances.put(
                        userId,
                        balances.getOrDefault(
                                userId,
                                BigDecimal.ZERO
                        ).subtract(
                                participant.getShareAmount()
                        )
                );

                userNames.put(
                        userId,
                        participant.getUser().getName()
                );
            }
        }

        List<BalanceResponse> responses =
                new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry
                : balances.entrySet()) {

            responses.add(
                    new BalanceResponse(
                            entry.getKey(),
                            userNames.get(entry.getKey()),
                            entry.getValue()
                    )
            );
        }

        return responses;
    }

    @Override
    public List<SettlementResponse> simplifyBalances(
            Long groupId
    ) {

        List<BalanceResponse> balances =
                getGroupBalances(groupId);

        List<BalanceResponse> creditors =
                new ArrayList<>();

        List<BalanceResponse> debtors =
                new ArrayList<>();

        for (BalanceResponse balance : balances) {

            if (balance.getBalance()
                    .compareTo(BigDecimal.ZERO) > 0) {

                creditors.add(balance);

            } else if (balance.getBalance()
                    .compareTo(BigDecimal.ZERO) < 0) {

                debtors.add(balance);
            }
        }

        List<SettlementResponse> settlements =
                new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < debtors.size() &&
                j < creditors.size()) {

            BalanceResponse debtor =
                    debtors.get(i);

            BalanceResponse creditor =
                    creditors.get(j);

            BigDecimal debtAmount =
                    debtor.getBalance().abs();

            BigDecimal creditAmount =
                    creditor.getBalance();

            BigDecimal settledAmount =
                    debtAmount.min(creditAmount);

            settlements.add(
                    new SettlementResponse(
                            debtor.getName(),
                            creditor.getName(),
                            settledAmount
                    )
            );

            debtor = new BalanceResponse(
                    debtor.getUserId(),
                    debtor.getName(),
                    debtor.getBalance()
                            .add(settledAmount)
            );

            creditor = new BalanceResponse(
                    creditor.getUserId(),
                    creditor.getName(),
                    creditor.getBalance()
                            .subtract(settledAmount)
            );

            debtors.set(i, debtor);

            creditors.set(j, creditor);

            if (debtor.getBalance()
                    .compareTo(BigDecimal.ZERO) == 0) {

                i++;
            }

            if (creditor.getBalance()
                    .compareTo(BigDecimal.ZERO) == 0) {

                j++;
            }
        }

        return settlements;
    }
}