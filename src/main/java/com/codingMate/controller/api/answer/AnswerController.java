package com.codingMate.controller.api.answer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.request.answer.AnswerCreateDto;
import com.codingMate.dto.request.answer.AnswerUpdateDto;
import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.dto.response.answer.AnswerListDto;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.service.answer.AnswerService;
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

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request, AnswerCreateDto dto) {
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
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
        try {
            AnswerDto readResult = answerService.read(id);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, readResult);
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
        List<AnswerDto> answerDtos = answerService.readAllByProgrammerId(id);
        if (answerDtos.size() == 0) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, answerService.read(id));
        } else {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerDtos);
        }
    }

    @PatchMapping("/{answerId}")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody AnswerUpdateDto dto, @PathVariable(name = "answerId") Long answerId) {
        log.info(dto.toString());
        Long idFromToken = JwtUtil.getIdFromToken(request);
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.update(idFromToken, answerId, dto));
        } catch (NotFoundAnswerException notFoundAnswerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundAnswerException.getMessage());
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