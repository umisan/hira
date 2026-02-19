package com.uminotech.hira.task;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.uminotech.hira.domain.Member;

public interface MemberRepository extends ListCrudRepository<Member, Long> {
    List<Member> findAllByActiveTrue();
}
