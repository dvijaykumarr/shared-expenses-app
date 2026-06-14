package com.expensesplitter.controller;

import com.expensesplitter.response.ImportRowResult;
import com.expensesplitter.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping("/csv")
    public List<ImportRowResult> importCSV(
            @RequestParam("file")
            MultipartFile file
    ) {

        return importService.importCSV(file);
    }
}