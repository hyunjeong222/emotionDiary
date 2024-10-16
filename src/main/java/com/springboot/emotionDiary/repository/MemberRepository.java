package com.springboot.emotionDiary.repository;

import com.springboot.emotionDiary.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserid(String userid);
    boolean existsByUserid(String userid);
}
