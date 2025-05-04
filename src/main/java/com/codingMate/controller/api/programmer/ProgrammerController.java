package com.codingMate.controller.api.programmer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.request.programmer.MyPateDto;
import com.codingMate.dto.request.programmer.ProgrammerCreateDto;
import com.codingMate.dto.request.programmer.ProgrammerUpdateDto;
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
    public ResponseEntity<?> create(@RequestBody ProgrammerCreateDto dto) {
        log.info("create {}", dto.getLoginId());
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.create(dto));
    }

    @GetMapping("/my-page")
    public ResponseEntity<?> myPage(HttpServletRequest request) {
        Long usernameFromToken = JwtUtil.getIdFromToken(request);
        MyPateDto myPateDto = myPageService.myPage(usernameFromToken);
        log.info("myPage {}", myPateDto.toString());
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, myPateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
        log.info("read {}", id);
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.read(id));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> readAll() {
        log.info("readAll");
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.readAll());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, ProgrammerUpdateDto dto) {
        log.info("update {}", dto.getLoginId());
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.update(id, dto));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        log.info("delete {}", id);
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.delete(id));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}