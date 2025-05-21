package com.codingMate.controller.api.auth;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.auth.dto.request.LoginRequest;
import com.codingMate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingMate.auth.dto.response.TokenDto;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.auth.service.LoginService;
import com.codingMate.programmer.service.ProgrammerService;
import com.codingMate.redis.RefreshTokenService;
import com.codingMate.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthController {
    private final TokenProvider tokenProvider;
    private final ProgrammerService programmerService;
    private final RefreshTokenService refreshTokenService;
    private final LoginService loginService;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.refresh}")
    private String refreshHeader;


    @Operation(summary = "로그인", description = "ID/PW로 로그인을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공."),
            @ApiResponse(responseCode = "404", description = "ID/PW가 틀렸습니다.")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var programmerResponse = loginService.login(loginRequest.loginId(), loginRequest.password());
        String accessToken = tokenProvider.createAccessToken(programmerResponse.id(), programmerResponse.authority());
        String refreshToken = tokenProvider.createRefreshToken(programmerResponse.id());

        refreshTokenService.saveToken(refreshToken, programmerResponse.id(), programmerResponse.authority());
        response.setHeader(header, accessToken);
        response.setHeader(refreshHeader, refreshToken);

        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }


    @Operation(summary = "회원가입", description = "회원가입을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공."),
            @ApiResponse(responseCode = "404", description = "요청 중 어떤 값이 유효하지 않은 값이라서 거부되었습니다.")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProgrammerCreateRequest programmerCreateRequest) {
        programmerService.create(programmerCreateRequest);
        return ResponseDto.toResponseEntity(ResponseMessage.CREATED);
    }


    @Operation(summary = "로그아웃", description = "로그아웃을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공."),
            @ApiResponse(responseCode = "404", description = "로그아웃 요청에 들어온 Refresh token이 유효하지 않습니다.")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        refreshTokenService.deleteRefreshToken(JwtUtil.getRefreshToken(request));
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }


    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공."),
            @ApiResponse(responseCode = "404", description = "유효한 사용자가 아니기에 탈퇴하지 못했습니다.")
    })
    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(HttpServletRequest request) {
        programmerService.delete(JwtUtil.getId(request));
        return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT);
    }


    @Operation(summary = "토큰 검증", description = "AccessToken이 유효한지 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검증 성공."),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")
    })
    @PostMapping("/access-token")
    public ResponseEntity<?> validateAccessToken(HttpServletRequest request) {
        tokenProvider.validateToken(JwtUtil.getAccessToken(request));
        return ResponseDto.toResponseEntity(ResponseMessage.AUTHORIZED);
    }


    @Operation(summary = "토큰 재발급", description = "토큰을 재발급 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 Refresh token이 아니기에 재발급에 실패앴습니다.")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto<TokenDto>> newRefreshToken(HttpServletRequest request) {
        var tokenDto = refreshTokenService.createAccessTokenFromRefreshToken(JwtUtil.getRefreshToken(request));
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, tokenDto);
    }
}