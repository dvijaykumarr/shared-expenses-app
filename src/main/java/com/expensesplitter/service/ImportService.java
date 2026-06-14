package com.expensesplitter.service;

import com.expensesplitter.response.ImportRowResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImportService {

    List<ImportRowResult> importCSV(
            MultipartFile file
    );
}