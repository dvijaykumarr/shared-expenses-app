package com.expensesplitter.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GroupMemberResponse {

    private Long userId;

    private String name;

    private LocalDate joinedAt;

    private LocalDate leftAt;

    private boolean active;
}