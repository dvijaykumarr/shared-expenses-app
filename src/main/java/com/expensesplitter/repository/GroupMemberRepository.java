package com.expensesplitter.repository;

import com.expensesplitter.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByGroupIdAndUserIdAndActiveTrue(Long groupId, Long userId);
}