package com.codingMate.repository.answer;

import com.codingMate.domain.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultAnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> readAnswersByProgrammerId(Long programmerId);
}