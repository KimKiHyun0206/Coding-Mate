package com.codingMate.controller.api.auth;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.request.programmer.LoginRequest;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.response.programmer.ProgrammerResponse;
import com.codingMate.dto.response.token.TokenDto;
import com.codingMate.exception.BusinessException;
import com.codingMate.exception.exception.jwt.ExpiredTokenException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.exception.exception.redis.InvalidRefreshTokenException;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.service.programmer.LoginService;
import com.codingMate.service.programmer.ProgrammerService;
import com.codingMate.service.redis.RefreshTokenService;
import com.codingMate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
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
        log.info("login({}, {})", loginRequest.getLoginId(), loginRequest.getPassword());
        ProgrammerResponse programmerResponse = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        String accessToken = tokenProvider.createAccessToken(programmerResponse.getId(), programmerResponse.getAuthority());
        String refreshToken = tokenProvider.createRefreshToken(programmerResponse.getId());

        refreshTokenService.saveToken(refreshToken, programmerResponse.getId(), programmerResponse.getAuthority());
        response.setHeader(header, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("TOKEN {} ", accessToken);
        log.info("REFRESH TOKEN {}", refreshToken);

        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, accessToken);
    }

    /**
     * @apiNote 회원가입 API
     * @param programmerCreateRequest 회원가입 정보를 가져오는 DTO
     * */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProgrammerCreateRequest programmerCreateRequest) throws IOException {
        log.info("register()");
        var programmerDto = programmerService.create(programmerCreateRequest);
        log.info("programmerDto {}", programmerDto.toString());

        return ResponseEntity.ok(ResponseMessage.SUCCESS);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.deleteRefreshToken(JwtUtil.getRefreshTokenFromHttpServletRequest(request));

        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, "로그아웃 성공했습니다");
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(HttpServletRequest request) {
        try {
            Long idFromToken = JwtUtil.getIdFromHttpServletRequest(request);
            boolean isDeleted = programmerService.delete(idFromToken);
            if (isDeleted) return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT, "요청한 계정을 삭제했습니다");
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, "요청한 계정을 삭제하지 못했습니다");
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        }
    }

    @GetMapping("/access-token")
    public ResponseEntity<?> validateAccessToken(HttpServletRequest request) {
        String accessToken = JwtUtil.getAccessTokenFromHttpServletRequest(request);
        log.info("validateAccessToken({})", accessToken);
        try {
            if (accessToken != null) {
                if (tokenProvider.validateToken(accessToken)) {
                    return ResponseEntity.ok(ResponseMessage.SUCCESS);
                }
            }
            return ResponseDto.toResponseEntity(ResponseMessage.UNAUTHORIZED, null);
        } catch (ExpiredTokenException businessException) {
            return ResponseDto.toResponseEntity(ResponseMessage.UNAUTHORIZED, businessException.getMessage());
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> newRefreshToken(HttpServletRequest request) {
        try {
            String refreshToken = JwtUtil.getRefreshTokenFromHttpServletRequest(request);
            log.info("newRefreshToken({})", refreshToken);
            if (refreshToken != null) {
                TokenDto tokenDto = refreshTokenService.createAccessTokenFromRefreshToken(refreshToken);
                return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, tokenDto);
            }
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, null);
        } catch (InvalidRefreshTokenException invalidRefreshTokenException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, invalidRefreshTokenException.getMessage());
        } catch (BusinessException businessException) {
            return ResponseDto.toResponseEntity(ResponseMessage.UNAUTHORIZED, businessException.getMessage());
        }
    }
}