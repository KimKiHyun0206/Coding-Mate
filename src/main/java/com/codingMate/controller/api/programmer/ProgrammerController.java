package com.codingMate.controller.api.programmer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.request.programmer.ProgrammerCreateDto;
import com.codingMate.dto.request.programmer.ProgrammerUpdateDto;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.service.programmer.ProgrammerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/programmer")
@RequiredArgsConstructor
public class ProgrammerController {
    private final ProgrammerService programmerService;

    @PostMapping
    public ResponseEntity<?> create(@NotNull ProgrammerCreateDto dto) {
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.create(dto));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
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
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.readAll());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, ProgrammerUpdateDto dto) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.update(dto));
        }catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        }catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.delete(id));
        }catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        }catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}