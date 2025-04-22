package com.codingMate.repository.programmer;

import com.codingMate.domain.programmer.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultProgrammerRepository extends JpaRepository<Programmer, Long> {
}