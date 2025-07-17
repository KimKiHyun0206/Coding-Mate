package com.codingmate.auth.service;

import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 스프링 시큐리티에서 로그인을 처리하기 위한 클래스.
 *
 * @author duskafka
 * */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailLoginService implements UserDetailsService {
    private final DefaultProgrammerRepository defaultProgrammerRepository;

    /**
     * 로그인 요청이 들어올 시 로그인을 요청을 처리해주는 메소드.
     *
     * <li>로그인 요청 외에 리프레쉬 토큰을 재발급할 때 사용자 정보를 가져오는 용도로도 사용된다.</li>
     *
     * @param username 로그인을 요청한 사용자의 {@code username}
     * @return 로그인을 성공하여 사용자 정보가 담긴 UerDetails
     * @see UserDetails 내부에 권한과 사용자 이름({@code username})을 담아서 비즈니스 로직을 처리할 수 있게 해준다.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("[UserDetailLoginService] loadUserByUsername({})", username);
        return defaultProgrammerRepository.findOneWithAuthoritiesByLoginId(username)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Programmer 엔티티를 User로 매핑해주는 메소드
     *
     * @param programmer 사용자 정보를 가진 엔티티
     * @return UserDetails를 상속한 클래스로 인증 정보를 가진다.
     */
    private User createUser(Programmer programmer) {
        return new User(
                programmer.getLoginId(),
                programmer.getPassword(),
                createAuthorities(programmer)
        );
    }

    /**
     * Programmer 엔티티 내부에 있는 authorities를 GrantedAuthority로 매핑해 List로 반환하는 메소드.
     *
     * @param programmer authorities를 가지고 있는 엔티티
     * @return 매핑하여 반환되는 {@code List<GrantedAuthority>}
     */
    private List<GrantedAuthority> createAuthorities(Programmer programmer) {
        return programmer.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
    }
}