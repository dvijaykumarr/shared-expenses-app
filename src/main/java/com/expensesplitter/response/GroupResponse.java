package com.expensesplitter.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GroupResponse {

    private Long id;
    private String name;
}