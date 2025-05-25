package com.codingmate.programmer.repository;

import com.codingmate.programmer.domain.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefaultProgrammerRepository extends JpaRepository<Programmer, Long> {
    Optional<Programmer> findById(Long id);
}