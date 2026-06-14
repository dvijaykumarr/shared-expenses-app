package com.expensesplitter.repository;

import com.expensesplitter.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByGroupIdAndUserIdAndActiveTrue(Long groupId, Long userId);
    Optional<GroupMember> findByGroupIdAndUserIdAndActiveTrue(Long groupId, Long userId);
}