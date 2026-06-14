package com.expensesplitter.controller;

import com.expensesplitter.request.AddMemberRequest;
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

    @PostMapping("/{groupId}/members")
    public String addMember(
            @PathVariable Long groupId,
            @RequestBody AddMemberRequest request
    ) {

        groupService.addMember(groupId, request);

        return "Member added successfully";
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public String removeMember(
            @PathVariable Long groupId,
            @PathVariable Long userId
    ) {

        groupService.removeMember(groupId, userId);

        return "Member removed successfully";
    }
}