package com.codingmate.refreshtoken.repository;

import com.codingmate.refreshtoken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByToken(String token);
}
