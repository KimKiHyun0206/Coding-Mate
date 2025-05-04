package com.codingMate.controller.api.programmer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.response.programmer.MyPageResponse;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.request.programmer.ProgrammerUpdateRequest;
import com.codingMate.exception.exception.jwt.UnMatchedAuthException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.service.programmer.MyPageService;
import com.codingMate.service.programmer.ProgrammerService;
import com.codingMate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/programmer")
@RequiredArgsConstructor
public class ProgrammerController {
    private final ProgrammerService programmerService;
    private final MyPageService myPageService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProgrammerCreateRequest dto) {
        log.info("create {}", dto.getLoginId());
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.create(dto));
    }

    @GetMapping("/my-page")
    public ResponseEntity<?> myPage(HttpServletRequest request) {
        Long usernameFromToken = JwtUtil.getIdFromToken(request);
        MyPageResponse myPageResponse = myPageService.myPage(usernameFromToken);
        log.info("myPage {}", myPageResponse.toString());
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, myPageResponse);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
        log.info("read ({})", id);
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.read(id));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }*/

    @GetMapping
    public ResponseEntity<?> readAll() {
        log.info("readAll()");
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.readAll());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> update(HttpServletRequest request,@RequestBody  ProgrammerUpdateRequest dto) {
        try {
            Long idFromToken = JwtUtil.getIdFromToken(request);
            log.info("update({}, {})", idFromToken, dto.toString());
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.update(idFromToken, dto));
        } catch (UnMatchedAuthException unMatchedAuthException) {
            return ResponseDto.toResponseEntity(ResponseMessage.UNAUTHORIZED, unMatchedAuthException.getMessage());
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request) {
        try {
            Long idFromToken = JwtUtil.getIdFromToken(request);
            log.info("delete({})", idFromToken);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.delete(idFromToken));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}