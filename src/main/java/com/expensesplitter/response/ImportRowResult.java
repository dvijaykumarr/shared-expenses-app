package com.expensesplitter.response;

import com.expensesplitter.enums.ImportStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImportRowResult {

    private int rowNumber;

    private ImportStatus status;

    private String message;
}