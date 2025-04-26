package com.codingMate.controller.api.comment;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.request.comment.CommentCreateDto;
import com.codingMate.dto.request.comment.CommentUpdateDto;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.comment.NotFoundCommentException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.service.answer.AnswerService;
import com.codingMate.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long programmerId, @RequestBody CommentCreateDto dto) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, commentService.create(programmerId, dto));
        } catch (NotFoundProgrammerException | NotFoundAnswerException businessException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, businessException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, commentService.read(id));
        } catch (NotFoundCommentException notFoundCommentException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundCommentException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/answer/{id}")
    public ResponseEntity<?> readByAnswer(@PathVariable Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, commentService.readByAnswerId(id));
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/programmer/{id}")
    public ResponseEntity<?> readByProgrammer(@PathVariable Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, commentService.readByProgrammerId(id));
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CommentUpdateDto dto) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, commentService.update(id, dto));
        } catch (NotFoundCommentException notFoundCommentException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundCommentException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long programmerId, @RequestParam Long answerId) {
        try {
            answerService.delete(programmerId, answerId);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, null);
        } catch (NotFoundCommentException notFoundCommentException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundCommentException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
