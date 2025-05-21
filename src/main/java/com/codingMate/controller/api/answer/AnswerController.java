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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    /**
     * @apiNote 풀이 생성 API
     * @param request 토큰을 받아오기 위한 매개변수
     * @param answerCreateRequest 생성할 문제의 정보를 가져옴
     * */
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

    /**
     * @apiNote 풀이 읽기 API
     * @implSpec 읽는 것은 토큰을 필수로 필요로하지 않지만 토큰을 가지고 있을 경우 수정할 권한을 가질 수 있도록 함
     * @param id 읽을 Answer 의  ID
     * @param request 토큰을 받아오기 위한 매개변수
     * */
    @GetMapping("/{answerId}")
    public ResponseEntity<ResponseDto<AnswerPageResponse>> read(@PathVariable(name = "answerId") Long id, HttpServletRequest request) {
        Long idFromToken = JwtUtil.getId(request);
        var answerPageDto = answerService.read(id, idFromToken);

        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                answerPageDto
        );
    }

    /**
     * @apiNote 모든 풀이 읽기 API
     * @implNote 추후 풀이의 수가 많아진다면 페이징 기능을 추가해야함
     * @implSpec 현재 페이징 기능이 없음
     * @param backjoonId 읽어올 문제의 backjoonId
     * @param language 읽어올 문제의 languageType
     * */
    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List<AnswerListResponse>>> readAll(
            @RequestParam(name = "language", required = false) LanguageType language,
            @RequestParam(name = "backjoonId", required = false) Long backjoonId
    ) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                answerService.readAllToListResponse(language, backjoonId)
        );
    }

    /**
     * @apiNote 특정 프로그래머가 작성한 풀이를 읽어오는 API, 프로그래머가 작성한 문제들을 읽어오기 위한 API 현재 사용되지 않지만 추후 기능 추가를 통해 사용할 것임
     * @implNote 이 기능은 헤더에서 ID 값을 받아오는 것으로 변경해야 할 필요가 있음
     * @implSpec 현재 페이징 기능이 없음
     * */
    @GetMapping("/programmer")
    public ResponseEntity<ResponseDto<List<AnswerListResponse>>> readByProgrammer(
            @RequestParam(name = "language", required = false) LanguageType language,
            @RequestParam(name = "backjoonId", required = false) Long backjoonId,
            HttpServletRequest request
    ) {
        Long idFromToken = JwtUtil.getId(request);

        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                answerService.readAllByProgrammerId(language, backjoonId, idFromToken)
        );
    }

    /**
     * @apiNote 풀이 수정 API
     * @implSpec request    에서 인증 정보를 가져오고 이를 바탕으로 업데이트 로직을 처리함
     * @param answerId 수정할 풀이의 ID
     * @param answerUpdateRequest 수정할 풀이의 정보
     * @param request 토큰을 받아오기 위한 매개변수
     * */
    @PatchMapping("/{answerId}")
    public ResponseEntity<ResponseDto<AnswerPageResponse>> update(
            HttpServletRequest request,
            @RequestBody AnswerUpdateRequest answerUpdateRequest,
            @PathVariable(name = "answerId") Long answerId
    ) {
        Long idFromToken = JwtUtil.getId(request);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.update(idFromToken, answerId, answerUpdateRequest));
    }

    /**
     * @apiNote 풀이 삭제 API
     * @param request 토큰을 받아오기 위한 매개변수
     * @param answerId 삭제할 풀이의 ID
     * */
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> delete(@PathVariable(name = "answerId") Long answerId, HttpServletRequest request) {
        Long idFromToken = JwtUtil.getId(request);
        answerService.delete(idFromToken, answerId);

        return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT);
    }
}