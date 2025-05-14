package com.codingMate.repository.programmer;

import com.codingMate.domain.programmer.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultProgrammerRepository extends JpaRepository<Programmer, Long> {
    Programmer findOneWithAuthoritiesByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    Programmer findByLoginId(String loginId);
    void deleteByLoginId(String loginId);
}