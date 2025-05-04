package com.codingMate.controller.api.auth;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.programmer.LoginDto;
import com.codingMate.dto.request.programmer.ProgrammerCreateDto;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.jwt.JwtFilter;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.service.programmer.LoginService;
import com.codingMate.service.programmer.ProgrammerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ProgrammerService programmerService;

    @Value("${jwt.header}") String header;

    @PostMapping("/login")
    public ResponseEntity<?> login(LoginDto loginDto, HttpServletResponse response) {
        log.info("login({}, {})", loginDto.getLoginId(), loginDto.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = programmerService.readIdByUserName(loginDto.getLoginId());

        String jwt = tokenProvider.createToken(authentication, userId);
        response.setHeader(header, "Bearer " + jwt);
        log.info("TOKEN {} ",jwt);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(ProgrammerCreateDto dto, HttpServletResponse response) throws IOException {
        log.info("register()");
        ProgrammerDto programmerDto = programmerService.create(dto);
        programmerDto.setId(null);
        programmerDto.setPassword(null);
        programmerDto.setLoginId(null);
        response.setStatus(200);
        response.sendRedirect("/login");
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerDto);
    }
}