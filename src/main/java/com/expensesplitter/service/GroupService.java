package com.expensesplitter.service;

import com.expensesplitter.request.AddMemberRequest;
import com.expensesplitter.request.GroupRequest;
import com.expensesplitter.response.GroupResponse;

public interface GroupService {

    GroupResponse createGroup(GroupRequest request);
    void addMember(Long groupId, AddMemberRequest request);
}