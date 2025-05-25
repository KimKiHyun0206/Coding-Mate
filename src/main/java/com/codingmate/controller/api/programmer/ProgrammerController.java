package com.codingmate.controller.api.programmer;

import com.codingmate.common.response.ResponseDto;
import com.codingmate.common.response.ResponseMessage;
import com.codingmate.programmer.dto.request.ProgrammerUpdateRequest;
import com.codingmate.programmer.dto.response.MyPageResponse;
import com.codingmate.programmer.service.ProgrammerService;
import com.codingmate.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/programmer")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerController {
    private final ProgrammerService programmerService;


    @Operation(summary = "존재하는 ID인지 확인", description = "ID가 중복되지 않도록 존재하는 ID인지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID가 중복되지 않습니다."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "409", description = "ID가 중복되었습니다.")
    })
    @GetMapping("/login-id/exists")
    public ResponseEntity<?> isExistLoginId(@RequestParam("loginId") String loginId) {
        programmerService.checkLoginIdAvailability(loginId);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }


    @Operation(summary = "마이페이지", description = "마이페이지 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 로딩 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "404", description = "유효한 요청이 아닙니다.")
    })
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<MyPageResponse>> myPage(HttpServletRequest request) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                programmerService.getProgrammerMyPageInfo(JwtUtil.getId(request))
        );
    }


    @Operation(summary = "유저 정보 수정", description = "유저 정보 수정을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "404", description = "수정 실패.")
    })
    @PatchMapping("/me")
    public ResponseEntity<?> update(
            @RequestBody ProgrammerUpdateRequest programmerUpdateRequest,
            HttpServletRequest request
    ) {
        programmerService.update(JwtUtil.getId(request), programmerUpdateRequest);

        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }
}