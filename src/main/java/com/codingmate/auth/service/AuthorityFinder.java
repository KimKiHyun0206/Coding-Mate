package com.codingmate.auth.service;

import com.codingmate.auth.domain.Authority;
import com.codingmate.auth.repository.AuthorityRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityFinder {
    private final AuthorityRepository authorityRepository;

    @Transactional(readOnly = true)
    public Authority getUserAuthority(String role) {
        return authorityRepository.getReferenceById(role);
    }
}