package com.codingMate.answer.repository;

import com.codingMate.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultAnswerRepository extends JpaRepository<Answer, Long> {
}