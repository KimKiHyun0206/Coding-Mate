package com.codingMate.controller.api.auth;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.request.programmer.LoginRequest;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.service.programmer.ProgrammerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Value("${jwt.header}")
    String header;


    /**
     * @apiNote 로그인 API
     * @implSpec UserDetails 를 사용하여 로그인한 후 로그인에 성공했다면 loginId로 id를 가져와 토큰에 담아 사용할 수 있게 함
     * @param loginRequest 로그인 요청에 사용되는 아이디와 비밀번호를 담은 DTO
     * @param response 응답에 토큰을 담아야하기 때문에 사용하는 매개변수
     * */
    @PostMapping("/login")
    public ResponseEntity<?> login(LoginRequest loginRequest, HttpServletResponse response) {
        log.info("login({}, {})", loginRequest.getLoginId(), loginRequest.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = programmerService.readIdByUserName(loginRequest.getLoginId());

        String jwt = tokenProvider.createToken(authentication, userId);
        response.setHeader(header, jwt);
        log.info("TOKEN {} ",jwt);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, jwt);
    }

    /**
     * @apiNote 회원가입 API
     * @param programmerCreateRequest 회원가입 정보를 가져오는 DTO
     * @param response 회원가입시 리다이렉션을 하기 위한 매개변수
     * */
    @PostMapping("/register")
    public ResponseEntity<?> register(ProgrammerCreateRequest programmerCreateRequest, HttpServletResponse response) throws IOException {
        log.info("register()");
        ProgrammerDto programmerDto = programmerService.create(programmerCreateRequest);
        log.info("programmerDto {}", programmerDto.toString());

        return ResponseEntity.ok(ResponseMessage.SUCCESS);
    }
}