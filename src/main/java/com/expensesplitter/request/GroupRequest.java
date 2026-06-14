package com.expensesplitter.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupRequest {

    @NotBlank(message = "Group name is required")
    private String name;

    private Long createdBy;
}