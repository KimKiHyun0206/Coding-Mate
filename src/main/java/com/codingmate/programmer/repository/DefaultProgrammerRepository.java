package com.codingmate.programmer.repository;

import com.codingmate.programmer.domain.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Programmer 엔티티를 데이터베이스에서 조회하기 위한 레포지토리
 *
 * @author duskafka
 * */
@Repository
public interface DefaultProgrammerRepository extends JpaRepository<Programmer, Long> {
    Optional<Programmer> findById(Long id);
}