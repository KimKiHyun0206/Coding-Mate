package com.codingmate.auth.service;

import com.codingmate.programmer.dto.response.ProgrammerResponse;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.LoginIdNotMatchException;
import com.codingmate.exception.exception.programmer.PasswordNotMatchException;
import com.codingmate.programmer.repository.ProgrammerReadRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 로그인을 위한 서비스
 *
 * <li>스프링 시큐리티가 지원하는 로그인을 사용하지 않기 때문에 구현함</li>
 *
 * @author duskafka
 * */
@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginService {
    private final ProgrammerReadRepository programmerReadRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 ID와 비밀번호를 이용해 로그인을 시도합니다.
     *
     * @param loginId  사용자 로그인 ID
     * @param password 사용자 비밀번호
     * @return 로그인에 성공하면 해당 Programmer의 정보를 담은 ProgrammerResponse 객체를 반환합니다.
     * @throws LoginIdNotMatchException 요청한 {@code loginId}에 해당하는 사용자를 찾을 수 없을 경우 발생합니다.
     * @throws PasswordNotMatchException 요청한 {@code password}가 해당 사용자의 비밀번호와 일치하지 않을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public ProgrammerResponse login(String loginId, String password) {
        log.info("[LoginService] Attempting login for user: {}", loginId);

        log.debug("[LoginService] Login request received. ID: {}, Password provided: [PROTECTED]", loginId);
        var programmer = programmerReadRepository.readByLoginId(loginId)
                .orElseThrow(() -> {
                    log.warn("[LoginService] Login failed: User ID not found. ID provided: {}", loginId);
                    return new LoginIdNotMatchException(
                            ErrorMessage.WRONG_ID,
                            String.format("요청한 ID %s는 존재하지 않는 ID입니다.", loginId)
                    );
                });
        log.debug("[LoginService] User found with ID: {}", programmer.getId());

        if (!passwordEncoder.matches(password, programmer.getPassword())) {
            log.warn("[LoginService] Login failed: Password mismatch for user ID: {}", loginId);
            throw new PasswordNotMatchException(
                    ErrorMessage.WRONG_PASSWORD,
                    String.format("요청한 비밀번호가 ID %s의 비밀번호와 일치하지 않습니다.", loginId)
            );
        }

        log.info("[LoginService] Login successful for user ID: {}", loginId);
        log.debug("[LoginService] Returning ProgrammerResponse for ID: {}", programmer.getId());

        return ProgrammerResponse.of(programmer);
    }
}