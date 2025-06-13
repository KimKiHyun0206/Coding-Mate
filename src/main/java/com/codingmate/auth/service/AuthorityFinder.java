package com.codingmate.auth.service;

import com.codingmate.auth.domain.Authority;
import com.codingmate.auth.repository.AuthorityRepository;
import com.codingmate.common.annotation.Explanation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "단순히 Authority를 Query",
        detail = "JPARepository를 사용한다",
        domain = "Authority",
        lastReviewed = "2025.06.05"
)
public class AuthorityFinder {
    private final AuthorityRepository authorityRepository;

    @Transactional(readOnly = true)
    public Authority getUserAuthority(String role) {
        return authorityRepository.getReferenceById(role);
    }
}