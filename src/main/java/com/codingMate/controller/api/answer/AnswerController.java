package com.codingMate.controller.api.answer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//전부 실행됨
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
            readResult.setId(null);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, readResult);
        } catch (NotFoundAnswerException notFoundAnswerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundAnswerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> readAll() {
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.readAll());
    }

    @GetMapping("/all/{backjoonId}")
    public ResponseEntity<?> readAllBackJoon(@PathVariable(name = "backjoonId") Long backjoonId) {
        List<AnswerListDto> answerListDtos = answerService.readByBackjoonId(backjoonId);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerListDtos);
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

    @PatchMapping("/{programmerId}")
    public ResponseEntity<?> update(HttpServletRequest request, AnswerUpdateDto dto) {
        Long idFromToken = JwtUtil.getIdFromToken(request);
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, answerService.update(idFromToken, dto));
        } catch (NotFoundAnswerException notFoundAnswerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundAnswerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id")Long answerId, HttpServletRequest request) {
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