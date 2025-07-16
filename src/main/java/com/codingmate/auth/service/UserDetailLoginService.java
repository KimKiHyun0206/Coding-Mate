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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailLoginService implements UserDetailsService {
    private final DefaultProgrammerRepository defaultProgrammerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("[UserDetailLoginService] loadUserByUsername({})", username);
        return defaultProgrammerRepository.findOneWithAuthoritiesByLoginId(username)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private User createUser(Programmer programmer) {
        return new User(
                programmer.getLoginId(),
                programmer.getPassword(),
                createAuthorities(programmer)
        );
    }

    private List<GrantedAuthority> createAuthorities(Programmer programmer) {
        return programmer.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
    }
}
