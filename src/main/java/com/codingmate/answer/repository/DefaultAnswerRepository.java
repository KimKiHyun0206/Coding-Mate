package com.codingmate.answer.repository;

import com.codingmate.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Answer에 대한 기본적인 JPA 메소드를 사용하기 위한 레포지토리.
 *
 * <li>기본적인 JPA 기능만 사용하며 최적화가 필요할 경우 다른 레포지토리에 Querydsl을 사용해야 함</li>
 *
 * @author duskafka
 */
@Repository
public interface DefaultAnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findOneWithProgrammerById(Long id);
}