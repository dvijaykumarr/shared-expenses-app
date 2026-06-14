package com.expensesplitter.mapper;

import com.expensesplitter.model.Group;
import com.expensesplitter.request.GroupRequest;
import com.expensesplitter.response.GroupResponse;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public Group toEntity(GroupRequest request) {

        Group group = new Group();

        group.setName(request.getName());

        return group;
    }

    public GroupResponse toResponse(Group group) {

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .build();
    }
}