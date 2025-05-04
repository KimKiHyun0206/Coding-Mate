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

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request, AnswerCreateRequest dto) {
        Long idFromToken = JwtUtil.getIdFromToken(request);
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.create(idFromToken, dto));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        try {
            Long idFromToken = JwtUtil.getIdFromToken(request);
            AnswerResponse readResult = answerService.read(id);
            AnswerPageResponse answerPageResponse = AnswerPageResponse.builder()
                    .code(readResult.getCode())
                    .title(readResult.getTitle())
                    .explanation(readResult.getExplanation())
                    .languageType(readResult.getLanguageType())
                    .backjoonId(readResult.getBackjoonId())
                    .id(idFromToken)
                    .programmerName(readResult.getProgrammer().getName())
                    .isRequesterIsOwner(Objects.equals(readResult.getProgrammer().getId(), idFromToken))
                    .build();
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerPageResponse);
        } catch (NotFoundAnswerException notFoundAnswerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundAnswerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> readAll(@RequestParam(name = "language", required = false) LanguageType language, @RequestParam(name = "backjoonId", required = false) Long backjoonId) {
        log.info("readAll()");
        if (language != null) {
            log.info("language {}", language);
        }
        if (backjoonId != null) {
            log.info("backjoonId {}", backjoonId);
        }
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.readAll(language, backjoonId));
    }

    @GetMapping("/programmer/{id}")
    public ResponseEntity<?> readByProgrammer(@PathVariable(name = "id") Long id) {
        List<AnswerResponse> answerResponses = answerService.readAllByProgrammerId(id);
        if (answerResponses.size() == 0) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, answerService.read(id));
        } else {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerResponses);
        }
    }

    @PatchMapping("/{answerId}")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody AnswerUpdateRequest dto, @PathVariable(name = "answerId") Long answerId) {
        try {
            log.info(dto.toString());
            Long idFromToken = JwtUtil.getIdFromToken(request);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.update(idFromToken, answerId, dto));
        } catch (AnswerAndProgrammerDoNotMatchException notFoundAnswerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundAnswerException.getMessage());

        } catch (NoTokenInHeaderException noTokenInHeaderException) {
            return ResponseDto.toResponseEntity(ResponseMessage.UNAUTHORIZED, noTokenInHeaderException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long answerId, HttpServletRequest request) {
        Long idFromToken = JwtUtil.getIdFromToken(request);
        try {
            answerService.delete(idFromToken, answerId);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, null);
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}