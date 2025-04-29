package com.codingMate.service.programmer;

import com.codingMate.domain.programmer.Programmer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgrammerDetailsService implements UserDetailsService {
    private final LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return createUser(loginService.getUserWithAuthorities(username));
    }

    private User createUser(Programmer programmer) {
        List<GrantedAuthority> grantedAuthorities = programmer.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        grantedAuthorities.forEach(a -> log.info(a.getAuthority()));
        return new User(programmer.getLoginId(), programmer.getPassword(), grantedAuthorities);
    }
}