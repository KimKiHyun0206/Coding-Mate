package com.codingmate.like.repository;

import com.codingmate.answer.domain.Answer;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // 특정 프로그래머가 특정 답변에 좋아요를 눌렀는지 확인 (existsBy...는 쿼리 최적화)
    boolean existsByProgrammerAndAnswer(Programmer programmer, Answer answer);

    // 특정 프로그래머가 특정 답변에 누른 좋아요 엔티티를 찾기
    Optional<Like> findByProgrammerAndAnswer(Programmer programmer, Answer answer);
}