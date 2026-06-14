package com.expensesplitter.controller;

import com.expensesplitter.request.GroupRequest;
import com.expensesplitter.response.GroupResponse;
import com.expensesplitter.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public GroupResponse createGroup(
            @Valid @RequestBody GroupRequest request
    ) {

        return groupService.createGroup(request);
    }
}