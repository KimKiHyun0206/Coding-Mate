package com.codingMate.controller.api.auth;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.programmer.LoginDto;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.jwt.JwtFilter;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.service.programmer.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final LoginService loginService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/login")
    public ResponseEntity<?> login(LoginDto loginDto, HttpServletResponse response) {
        log.info("login({})", loginDto.getLoginId() + ", " + loginDto.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, jwt);
    }
}