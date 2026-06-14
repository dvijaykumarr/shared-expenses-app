package com.expensesplitter.model;

import com.expensesplitter.enums.Currency;
import com.expensesplitter.enums.SplitType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal exchangeRate;

    private BigDecimal normalizedAmount;

    @Enumerated(EnumType.STRING)
    private SplitType splitType;

    private LocalDate expenseDate;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}