package com.codingmate.auth.service;

import com.codingmate.auth.domain.Authority;
import com.codingmate.auth.repository.AuthorityRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 단순히 Authority를 데이터베이스에서 조회하기 위한 Finder 클래스
 *
 * <li>프록시 객체로 가져온다</li>
 * <li>프록시 객체로 가져오기 때문에 객체를 사용하려고 하면 지연 로딩이 발생한다</li>
 *
 * @author duskafka
 * */
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