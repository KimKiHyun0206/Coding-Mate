package com.codingMate.repository.tip;

import com.codingMate.domain.tip.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultTipRepository extends JpaRepository<Tip, Long> {
}