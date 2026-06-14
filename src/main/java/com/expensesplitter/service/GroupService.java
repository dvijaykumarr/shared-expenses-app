package com.expensesplitter.service;

import com.expensesplitter.request.AddMemberRequest;
import com.expensesplitter.request.GroupRequest;
import com.expensesplitter.response.GroupMemberResponse;
import com.expensesplitter.response.GroupResponse;

import java.util.List;

public interface GroupService {

    GroupResponse createGroup(GroupRequest request);
    void addMember(Long groupId, AddMemberRequest request);
    void removeMember(Long groupId, Long userId);
    List<GroupMemberResponse> getGroupMembers(Long groupId);
}