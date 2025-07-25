package com.codingmate.service;

import com.codingmate.auth.domain.Authority;
import com.codingmate.auth.service.AuthorityFinder;
import com.codingmate.email.service.EmailVerificationService;
import com.codingmate.email.service.EmailVerificationTokenGenerator;
import com.codingmate.exception.exception.email.EmailNotVerificationException;
import com.codingmate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingmate.jwt.TokenProvider;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.programmer.service.ProgrammerService;
import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
public class LoginServiceTest {

    @Autowired
    private ProgrammerService programmerService;

    @Autowired
    private DefaultProgrammerRepository programmerRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EmailVerificationTokenGenerator emailVerificationTokenGenerator;

    @Autowired
    private AuthorityFinder authorityFinder;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final ProgrammerCreateRequest createRequest = new ProgrammerCreateRequest(
            "new_login_id",
            "github_id",
            "qwlkekd123!!!@#",
            "홍길동",
            "hong@gmail.com"
    );

    /**
     * 회원가입에 성공하는 테스트 코드.
     */
    @Test
    void register_Success() {
        assertThatCode(() -> {
            // 이메일 인증을 임의로 처리함.
            String token = emailVerificationTokenGenerator.generateEmailVerificationToken(createRequest.email());
            emailVerificationService.verify(token);

            programmerService.create(createRequest);
        }).doesNotThrowAnyException();
    }

    /**
     * 이메일 인증이 처리되지 않아 회원가입에 실패하는 테스트 코드.
     */
    @Test
    void register_Fail_WhenEmailNotVerified() {
        assertThatCode(() -> {
            programmerService.create(createRequest);
        }).isInstanceOf(EmailNotVerificationException.class);
    }

    /**
     * login_id가 중복되어 회원가입에 실패하는 테스트.
     */
    @Test
    void register_Fail_WhenDuplicateLoginId() {
        assertThatCode(() -> {
            programmerRepository.save(Programmer.toEntity(createRequest, new HashSet<>()));
            programmerService.create(createRequest);
        }).isInstanceOf(DuplicateProgrammerLoginIdException.class);
    }

    /**
     * 로그인에 성공하고 토큰까지 발급하는 테스트.
     * */
    @Test
    void login_Success() {
        assertThatCode(() -> {
            // 회원가입 했다고 가정할 코드
            Set<Authority> authorities = new HashSet<>();
            authorities.add(authorityFinder.getUserAuthority("ROLE_USER"));
            Programmer save = programmerRepository.save(Programmer.toEntity(createRequest, authorities));

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(createRequest.loginId(), createRequest.password())
            );

            assertThat(authentication).isNotNull(); // 인증이 성공했는지 확인
            assertThat(authentication.isAuthenticated()).isTrue(); // 사용자가 인증되었는지 확인
            assertThat(authentication.getName()).isEqualTo(createRequest.loginId()); // 올바른 주체(principal)가 설정되었는지 확인

            String accessToken = tokenProvider.createAccessToken(authentication);
            assertThat(accessToken).isNotNull(); // 액세스 토큰은 null이 아니어야 함
            assertThat(accessToken).isNotEmpty(); // 액세스 토큰은 비어있지 않아야 함

            RefreshTokenIssueResponse refreshToken = tokenProvider.createRefreshToken(authentication.getName());
            assertThat(refreshToken).isNotNull(); // 리프레시 토큰은 null이 아니어야 함
            assertThat(refreshToken.refreshToken()).isNotNull(); // 리프레시 토큰 문자열 자체는 null이 아니어야 함
            assertThat(refreshToken.refreshToken()).isNotEmpty(); // 리프레시 토큰 문자열 자체는 비어있지 않아야 함
        }).doesNotThrowAnyException();
    }

    /**
     * login_id가 일치하지 않아서 로그인에 실패하는 테스트.
     * */
    @Test
    void login_Fail_WhenLoginIdWrong() {
        assertThatCode(() -> {
            // 회원가입 했다고 가정할 코드
            Set<Authority> authorities = new HashSet<>();
            authorities.add(authorityFinder.getUserAuthority("ROLE_USER"));
            programmerRepository.save(Programmer.toEntity(createRequest, authorities));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("wrong_login_id", createRequest.password())
            );
        }).isInstanceOf(BadCredentialsException.class);
    }

    /**
     * password가 일치하지 않아서 로그인에 실패하는 테스트.
     * */
    @Test
    void login_Fail_WhenPasswordWrong() {
        assertThatCode(() -> {
            // 회원가입 했다고 가정할 코드
            Set<Authority> authorities = new HashSet<>();
            authorities.add(authorityFinder.getUserAuthority("ROLE_USER"));
            programmerRepository.save(Programmer.toEntity(createRequest, authorities));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(createRequest.loginId(), "wrong_password")
            );
        }).isInstanceOf(BadCredentialsException.class);
    }
}