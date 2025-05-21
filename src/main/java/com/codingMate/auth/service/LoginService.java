package com.codingMate.auth.service;

import com.codingMate.programmer.domain.Programmer;
import com.codingMate.programmer.dto.response.ProgrammerResponse;
import com.codingMate.exception.dto.ErrorMessage;
import com.codingMate.exception.exception.programmer.LoginIdNotMatchException;
import com.codingMate.exception.exception.programmer.PasswordNotMatchException;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.programmer.repository.CustomProgrammerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final CustomProgrammerRepository programmerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    /**
     * @implNote 아이디로 유저를 찾는다
     * */
    @Transactional(readOnly = true)
    public Programmer getUserWithAuthorities(String loginId) {
        log.info("getUserWithAuthorities({})", loginId);
        return programmerRepository.readByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public ProgrammerResponse login(String loginId, String password) {
        log.info("login({}, {})", loginId, password);
        Programmer programmer = programmerRepository.readByLoginId(loginId);
        if (programmer == null) throw new LoginIdNotMatchException(ErrorMessage.WRONG_ID, "요청한 ID가 일치하지 않습니다");
        if (!passwordEncoder.matches(password, programmer.getPassword())) {
            throw new PasswordNotMatchException(ErrorMessage.WRONG_PASSWORD, "요청한 비밀번호가 일치하지 않습니다");
        }
        return ProgrammerResponse.from(programmer);
    }
}