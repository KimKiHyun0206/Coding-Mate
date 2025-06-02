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

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginService {
    private final ProgrammerReadRepository programmerReadRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public ProgrammerResponse login(String loginId, String password) {
        log.info("[SYSTEM] login({}, {})", loginId, password);
        var programmer = programmerReadRepository.readByLoginId(loginId)
                .orElseThrow(() ->
                        new LoginIdNotMatchException(
                                ErrorMessage.WRONG_ID,
                                String.format("요청한 ID %s는 존재하지 않는 ID입니다.", loginId)
                        )
                );

        if (!passwordEncoder.matches(password, programmer.getPassword())) {
            throw new PasswordNotMatchException(
                    ErrorMessage.WRONG_PASSWORD,
                    String.format("요청한 비밀번호 %s가 요청한 ID의 비밀번호와 일치하지 않습니다.", password)
            );
        }
        return ProgrammerResponse.of(programmer);
    }
}