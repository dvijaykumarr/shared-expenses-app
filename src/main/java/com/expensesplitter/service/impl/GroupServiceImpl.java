package com.expensesplitter.service.impl;

import com.expensesplitter.mapper.GroupMapper;
import com.expensesplitter.model.Group;
import com.expensesplitter.model.GroupMember;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.GroupMemberRepository;
import com.expensesplitter.repository.GroupRepository;
import com.expensesplitter.repository.UserRepository;
import com.expensesplitter.request.AddMemberRequest;
import com.expensesplitter.request.GroupRequest;
import com.expensesplitter.response.GroupResponse;
import com.expensesplitter.service.GroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    @Override
    @Transactional
    public GroupResponse createGroup(GroupRequest request) {

        User currentUser = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Group group = groupMapper.toEntity(request);

        group.setCreatedAt(LocalDateTime.now());

        Group savedGroup = groupRepository.save(group);

        GroupMember groupMember = GroupMember.builder()
                .group(savedGroup)
                .user(currentUser)
                .joinedAt(LocalDate.now())
                .active(true)
                .build();

        groupMemberRepository.save(groupMember);

        return groupMapper.toResponse(savedGroup);
    }

    @Override
    @Transactional
    public void addMember(
            Long groupId,
            AddMemberRequest request
    ) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean alreadyMember =
                groupMemberRepository
                        .existsByGroupIdAndUserIdAndActiveTrue(
                                groupId,
                                request.getUserId()
                        );

        if (alreadyMember) {

            throw new RuntimeException(
                    "User is already an active member"
            );
        }

        GroupMember groupMember = GroupMember.builder()
                .group(group)
                .user(user)
                .joinedAt(LocalDate.now())
                .active(true)
                .build();

        groupMemberRepository.save(groupMember);
    }

    @Override
    @Transactional
    public void removeMember(
            Long groupId,
            Long userId
    ) {

        GroupMember groupMember =
                groupMemberRepository
                        .findByGroupIdAndUserIdAndActiveTrue(
                                groupId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Active group membership not found"
                                ));

        groupMember.setActive(false);

        groupMember.setLeftAt(LocalDate.now());

        groupMemberRepository.save(groupMember);
    }
}