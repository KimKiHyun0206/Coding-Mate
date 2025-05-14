package com.codingMate.controller.api.answer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.request.answer.AnswerCreateRequest;
import com.codingMate.dto.request.answer.AnswerUpdateRequest;
import com.codingMate.dto.response.answer.AnswerResponse;
import com.codingMate.dto.response.answer.AnswerPageResponse;
import com.codingMate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.jwt.NoTokenInHeaderException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.service.answer.AnswerService;
import com.codingMate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


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
    public ResponseEntity<?> create(AnswerCreateRequest answerCreateRequest, HttpServletRequest request) {
        try {
            log.info("create({}, {})", request.toString(), answerCreateRequest.toString());
            String loginIdFromToken = JwtUtil.getLoginIdFromToken(request);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.create(loginIdFromToken, answerCreateRequest));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @apiNote 풀이 읽기 API
     * @implSpec 읽는 것은 토큰을 필수로 필요로하지 않지만 토큰을 가지고 있을 경우 수정할 권한을 가질 수 있도록 함
     * @param id 읽을 Answer 의  ID
     * @param request 토큰을 받아오기 위한 매개변수
     * */
    @GetMapping("/{answerId}")
    public ResponseEntity<?> read(@PathVariable(name = "answerId") Long id, HttpServletRequest request) {
        Long idFromToken = JwtUtil.getIdFromHttpServletRequest(request);
        try {
            log.info("read({})", id);
            AnswerPageResponse answerPageDto = answerService.read(id).toAnswerPageDto();
            if (idFromToken != null) {
                answerPageDto.setIsRequesterIsOwner(Objects.equals(answerPageDto.getProgrammerId(), idFromToken));
            }
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerPageDto);
        } catch (NotFoundAnswerException notFoundAnswerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundAnswerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @apiNote 모든 풀이 읽기 API
     * @implNote 추후 풀이의 수가 많아진다면 페이징 기능을 추가해야함
     * @implSpec 현재 페이징 기능이 없음
     * @param backjoonId 읽어올 문제의 backjoonId
     * @param language 읽어올 문제의 languageType
     * */
    @GetMapping("/all")
    public ResponseEntity<?> readAll(@RequestParam(name = "language", required = false) LanguageType language, @RequestParam(name = "backjoonId", required = false) Long backjoonId) {
        log.info("readAll()");
        log.info("language {}", language);
        log.info("backjoonId {}", backjoonId);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.readAllToListResponse(language, backjoonId));
    }

    /**
     * @apiNote 특정 프로그래머가 작성한 풀이를 읽어오는 API, 프로그래머가 작성한 문제들을 읽어오기 위한 API 현재 사용되지 않지만 추후 기능 추가를 통해 사용할 것임
     * @implNote 이 기능은 헤더에서 ID 값을 받아오는 것으로 변경해야 할 필요가 있음
     * @implSpec 현재 페이징 기능이 없음
     * */
    @GetMapping("/programmer")
    public ResponseEntity<?> readByProgrammer(@RequestParam(name = "language", required = false) LanguageType language, @RequestParam(name = "backjoonId", required = false) Long backjoonId, HttpServletRequest request) {
        String loginIdFromToken = JwtUtil.getLoginIdFromToken(request);
        log.info("readByProgrammer({})", loginIdFromToken);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.readAllByProgrammerId(language, backjoonId, loginIdFromToken));
    }

    /**
     * @apiNote 풀이 수정 API
     * @implSpec request    에서 인증 정보를 가져오고 이를 바탕으로 업데이트 로직을 처리함
     * @param answerId 수정할 풀이의 ID
     * @param answerUpdateRequest 수정할 풀이의 정보
     * @param request 토큰을 받아오기 위한 매개변수
     * */
    @PatchMapping("/{answerId}")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody AnswerUpdateRequest answerUpdateRequest, @PathVariable(name = "answerId") Long answerId) {
        try {
            log.info(answerUpdateRequest.toString());
            Long idFromToken = JwtUtil.getIdFromHttpServletRequest(request);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.update(idFromToken, answerId, answerUpdateRequest));
        } catch (AnswerAndProgrammerDoNotMatchException notFoundAnswerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundAnswerException.getMessage());

        } catch (NoTokenInHeaderException noTokenInHeaderException) {
            return ResponseDto.toResponseEntity(ResponseMessage.UNAUTHORIZED, noTokenInHeaderException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @apiNote 풀이 삭제 API
     * @param request 토큰을 받아오기 위한 매개변수
     * @param answerId 삭제할 풀이의 ID
     * */
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> delete(@PathVariable(name = "answerId") Long answerId, HttpServletRequest request) {
        try {
            Long idFromToken = JwtUtil.getIdFromHttpServletRequest(request);
            boolean isDeleted = answerService.delete(idFromToken, answerId);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, isDeleted);
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}