package com.codingMate.controller.api.auth;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.request.programmer.LoginRequest;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.response.token.TokenDto;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.service.programmer.LoginService;
import com.codingMate.service.programmer.ProgrammerService;
import com.codingMate.service.redis.RefreshTokenService;
import com.codingMate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final ProgrammerService programmerService;
    private final RefreshTokenService refreshTokenService;
    private final LoginService loginService;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.refresh}")
    private String refreshHeader;


    /**
     * @apiNote 로그인 API
     * @implSpec UserDetails 를 사용하여 로그인한 후 로그인에 성공했다면 loginId로 id를 가져와 토큰에 담아 사용할 수 있게 함
     * @param loginRequest 로그인 요청에 사용되는 아이디와 비밀번호를 담은 DTO
     * @param response 응답에 토큰을 담아야하기 때문에 사용하는 매개변수
     * */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        var programmerResponse = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        String accessToken = tokenProvider.createAccessToken(programmerResponse.getId(), programmerResponse.getAuthority());
        String refreshToken = tokenProvider.createRefreshToken(programmerResponse.getId());

        refreshTokenService.saveToken(refreshToken, programmerResponse.getId(), programmerResponse.getAuthority());
        response.setHeader(header, accessToken);
        response.setHeader(refreshHeader, refreshToken);

        return ResponseDto.toResponseEntity(ResponseMessage.CREATED);
    }

    /**
     * @apiNote 회원가입 API
     * @param programmerCreateRequest 회원가입 정보를 가져오는 DTO
     * */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProgrammerCreateRequest programmerCreateRequest) throws IOException {
        programmerService.create(programmerCreateRequest);
        return ResponseDto.toResponseEntity(ResponseMessage.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        refreshTokenService.deleteRefreshToken(JwtUtil.getRefreshToken(request));
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(HttpServletRequest request) {
        programmerService.delete(JwtUtil.getId(request));
        return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT);
    }

    @PostMapping("/access-token")
    public ResponseEntity<?> validateAccessToken(HttpServletRequest request) {
        tokenProvider.validateToken(JwtUtil.getAccessToken(request));
        return ResponseDto.toResponseEntity(ResponseMessage.AUTHORIZED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto<TokenDto>> newRefreshToken(HttpServletRequest request) {
        var tokenDto = refreshTokenService.createAccessTokenFromRefreshToken(JwtUtil.getRefreshToken(request));
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, tokenDto);
    }
}