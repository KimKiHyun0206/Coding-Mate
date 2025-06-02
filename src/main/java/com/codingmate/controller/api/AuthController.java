package com.codingmate.controller.api;

import com.codingmate.auth.service.TokenService;
import com.codingmate.common.response.ResponseDto;
import com.codingmate.common.response.ResponseMessage;
import com.codingmate.auth.dto.request.LoginRequest;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.jwt.TokenProvider;
import com.codingmate.auth.service.LoginService;
import com.codingmate.programmer.service.ProgrammerService;
import com.codingmate.redis.RefreshTokenService;
import com.codingmate.util.CookieUtil;
import com.codingmate.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final ProgrammerService programmerService;
    private final RefreshTokenService refreshTokenService;
    private final LoginService loginService;
    private final TokenService tokenService;

    private final String ACCESS_TOKEN_HEADER_NAME;
    private final String REFRESH_TOKEN_COOKIE_NAME;

    public AuthController(
            ProgrammerService programmerService,
            RefreshTokenService refreshTokenService,
            LoginService loginService,
            TokenService tokenService,
            JWTProperties jwtProperties
    ) {
        this.programmerService = programmerService;
        this.refreshTokenService = refreshTokenService;
        this.loginService = loginService;
        this.tokenService = tokenService;
        this.ACCESS_TOKEN_HEADER_NAME = jwtProperties.getAccessTokenHeader();
        this.REFRESH_TOKEN_COOKIE_NAME = jwtProperties.getRefreshTokenCookie();
    }


    @Operation(summary = "로그인", description = "ID/PW로 로그인을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공."),
            @ApiResponse(responseCode = "400", description = "ID/PW가 틀렸습니다.")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var programmer = loginService.login(loginRequest.loginId(), loginRequest.password());
        var tokens = tokenService.generateToken(programmer.id(), programmer.authority());

        refreshTokenService.saveToken(
                tokens.refreshToken(),
                programmer.id(),
                programmer.authority()
        );

        var cookie = CookieUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME, tokens.refreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());                  //헤더에 쿠키 추가
        response.addHeader(ACCESS_TOKEN_HEADER_NAME, tokens.accessToken());             //헤더에 액세스 토큰 추가

        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }


    @Operation(summary = "회원가입", description = "회원가입을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공."),
            @ApiResponse(responseCode = "400", description = "요청 중 어떤 값이 유효하지 않은 값이라서 거부되었습니다.")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody ProgrammerCreateRequest programmerCreateRequest) {
        programmerService.create(programmerCreateRequest);
        return ResponseDto.toResponseEntity(ResponseMessage.CREATED);
    }


    @Operation(summary = "로그아웃", description = "로그아웃을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공."),
            @ApiResponse(responseCode = "401", description = "로그아웃 요청에 들어온 Refresh token이 유효하지 않습니다.")
    })
    @DeleteMapping("/sign-out")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh-token") String refreshToken, HttpServletResponse response) {
        log.info(refreshToken);
        refreshTokenService.deleteRefreshToken(refreshToken);
        var cookie = CookieUtil.deleteCookie(REFRESH_TOKEN_COOKIE_NAME);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }


    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 사용자가 아니기에 탈퇴하지 못했습니다.")
    })
    @DeleteMapping("/me")
    public ResponseEntity<?> withdrawal(HttpServletRequest request) {
        programmerService.delete(JwtUtil.getId(request));
        return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT);
    }


    @Operation(summary = "토큰 검증", description = "AccessToken이 유효한지 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검증 성공."),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")
    })
    @GetMapping("/tokens")
    public ResponseEntity<?> validateAccessToken(HttpServletRequest request) {
        tokenService.validateToken(JwtUtil.getAccessToken(request));
        return ResponseDto.toResponseEntity(ResponseMessage.AUTHORIZED);
    }


    @Operation(summary = "토큰 재발급", description = "토큰을 재발급 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 Refresh token이 아니기에 재발급에 실패앴습니다.")
    })
    @PostMapping("/tokens")
    public ResponseEntity<?> newRefreshToken(
            @CookieValue(name = "refresh-token") String refreshToken,
            HttpServletResponse response
    ) {
        var tokenDto = refreshTokenService.refreshTokens(refreshToken);
        var cookie = CookieUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME, tokenDto.refreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader(ACCESS_TOKEN_HEADER_NAME, tokenDto.accessToken());
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }
}