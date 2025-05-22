package com.codingMate.controller.api.answer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.answer.domain.vo.LanguageType;
import com.codingMate.answer.dto.request.AnswerCreateRequest;
import com.codingMate.answer.dto.request.AnswerUpdateRequest;
import com.codingMate.answer.dto.response.AnswerCreateResponse;
import com.codingMate.answer.dto.response.AnswerListResponse;
import com.codingMate.answer.dto.response.AnswerPageResponse;
import com.codingMate.answer.service.AnswerService;
import com.codingMate.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerController {
    private final AnswerService answerService;

    @Operation(summary = "풀이 생성", description = "새로운 풀이를 생성한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "풀이 생성 성공."),
            @ApiResponse(responseCode = "404", description = "풀이 생성 전 인증에 실패했습니다.")
    })
    @PostMapping
    public ResponseEntity<ResponseDto<AnswerCreateResponse>> create(
            @RequestBody AnswerCreateRequest answerCreateRequest,
            HttpServletRequest request
    ) {
        Long idFromToken = JwtUtil.getId(request);

        return ResponseDto.toResponseEntity(
                ResponseMessage.CREATED,
                answerService.create(idFromToken, answerCreateRequest)
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
            HttpServletRequest request
    ) {
        Long idFromToken = JwtUtil.getId(request);
        var answerPageDto = answerService.read(id, idFromToken);

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
                answerService.readAllToListResponse(language, backjoonId, pageable)
        );
    }


    @Operation(summary = "요청한 유저가 작성한 풀이 조회", description = "자신이 작성한 풀이만 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "풀이 조회 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다.")
    })
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<Page<AnswerListResponse>>> readByProgrammer(
            @RequestParam(name = "language", required = false) LanguageType language,
            @RequestParam(name = "backjoonId", required = false) Long backjoonId,
            HttpServletRequest request,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Long idFromToken = JwtUtil.getId(request);

        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                answerService.readAllByProgrammerId(language, backjoonId, idFromToken, pageable)
        );
    }


    @Operation(summary = "풀이 수정", description = "작성된 풀이를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "풀이 수정 성공."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "404", description = "유효한 풀이 ID가 아닙니다.")
    })
    @PatchMapping("/{answerId}")
    public ResponseEntity<?> update(
            HttpServletRequest request,
            @RequestBody AnswerUpdateRequest answerUpdateRequest,
            @PathVariable(name = "answerId") Long answerId
    ) {
        Long idFromToken = JwtUtil.getId(request);
        answerService.update(idFromToken, answerId, answerUpdateRequest);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }


    @Operation(summary = "풀이 삭제", description = "작성된 풀이를 id값으로 풀이 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "풀이 삭제 성공."),
            @ApiResponse(responseCode = "400", description = "요청자가 작성한 풀이가 아닙니다."),
            @ApiResponse(responseCode = "401", description = "유효한 인증이 아니어 실패앴습니다."),
            @ApiResponse(responseCode = "404", description = "유효한 풀이 ID가 아닙니다.")
    })
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> delete(@PathVariable(name = "answerId") Long answerId, HttpServletRequest request) {
        Long idFromToken = JwtUtil.getId(request);
        answerService.delete(idFromToken, answerId);

        return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT);
    }
}