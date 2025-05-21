package com.codingMate.programmer.repository;

import com.codingMate.programmer.domain.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultProgrammerRepository extends JpaRepository<Programmer, Long> {
    Programmer findOneWithAuthoritiesByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    Programmer findByLoginId(String loginId);
    void deleteByLoginId(String loginId);
}