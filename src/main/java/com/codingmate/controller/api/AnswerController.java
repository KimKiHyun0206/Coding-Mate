package com.codingmate.controller.api;

import com.codingmate.common.response.ResponseDto;
import com.codingmate.common.response.ResponseMessage;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.codingmate.answer.dto.response.AnswerCreateResponse;
import com.codingmate.answer.dto.response.AnswerListResponse;
import com.codingmate.answer.dto.response.AnswerPageResponse;
import com.codingmate.answer.service.AnswerService;
import com.codingmate.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerController {
    private final AnswerService answerService;

    @RolesAllowed("hasRole('ROLE_USER')")
    @Operation(summary = "풀이 생성", description = "새로운 풀이를 생성한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "풀이 생성 성공."),
            @ApiResponse(responseCode = "404", description = "풀이 생성 전 인증에 실패했습니다.")
    })
    @PostMapping
    public ResponseEntity<ResponseDto<AnswerCreateResponse>> create(
            @RequestBody AnswerCreateRequest answerCreateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.CREATED,
                answerService.create(
                        userDetails.getUsername(),
                        answerCreateRequest
                )
        );
    }


    @Operation(summary = "풀이 읽기", description = "작성된 풀이를 id값으로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "풀이 조회 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "404", description = "유효한 풀이 ID가 아닙니다.")
    })
    @GetMapping("/{answerId}")
    public ResponseEntity<ResponseDto<AnswerPageResponse>> read(
            @PathVariable(name = "answerId") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        var answerPageDto = answerService.read(
                id,
                userDetails.getUsername()
        );

        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                answerPageDto
        );
    }


    @Operation(summary = "전체 풀이 읽기", description = "전체 풀이를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "풀이 조회 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다.")
    })
    @GetMapping
    public ResponseEntity<ResponseDto<Page<AnswerListResponse>>> readAll(
            @RequestParam(name = "language", required = false) LanguageType language,
            @RequestParam(name = "backjoonId", required = false) Long backjoonId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                answerService.readAllToListResponse(
                        language,
                        backjoonId,
                        pageable
                )
        );
    }

    @RolesAllowed("hasRole('ROLE_USER')")
    @Operation(summary = "요청한 유저가 작성한 풀이 조회", description = "자신이 작성한 풀이만 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "풀이 조회 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다.")
    })
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<Page<AnswerListResponse>>> readByProgrammer(
            @RequestParam(name = "language", required = false) LanguageType language,
            @RequestParam(name = "backjoonId", required = false) Long backjoonId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                answerService.readAllByProgrammerId(
                        language,
                        backjoonId,
                        userDetails.getUsername(),
                        pageable
                )
        );
    }

    @RolesAllowed("hasRole('ROLE_USER')")
    @Operation(summary = "풀이 수정", description = "작성된 풀이를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "풀이 수정 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "404", description = "유효한 풀이 ID가 아닙니다.")
    })
    @PatchMapping("/{answerId}")
    public ResponseEntity<?> update(
            @RequestBody AnswerUpdateRequest answerUpdateRequest,
            @PathVariable(name = "answerId") Long answerId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        answerService.update(userDetails.getUsername(), answerId, answerUpdateRequest);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }

    @RolesAllowed("hasRole('ROLE_USER')")
    @Operation(summary = "풀이 삭제", description = "작성된 풀이를 id값으로 풀이 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "풀이 삭제 성공."),
            @ApiResponse(responseCode = "400", description = "요청자가 작성한 풀이가 아닙니다."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "404", description = "유효한 풀이 ID가 아닙니다.")
    })
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> delete(
            @PathVariable(name = "answerId") Long answerId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        answerService.delete(userDetails.getUsername(), answerId);

        return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT);
    }
}