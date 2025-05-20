package com.codingMate.controller.api.programmer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.request.programmer.ProgrammerUpdateRequest;
import com.codingMate.dto.response.programmer.MyPageResponse;
import com.codingMate.dto.response.programmer.ProgrammerCreateResponse;
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

    /**
     * @apiNote 회원 생성 API
     * */
    @PostMapping
    public ResponseEntity<ResponseDto<ProgrammerCreateResponse>> create(@RequestBody ProgrammerCreateRequest dto) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.CREATED,
                programmerService.create(dto)
        );
    }

    /**
     * @apiNote 회원 생성 전에 아이디 중복 확인 버튼으로 아이디가 중복되는지 검사할 API
     * */
    @GetMapping("/check-id")
    public ResponseEntity<?> isExistLoginId(@RequestParam("loginId") String loginId) {
        programmerService.isExistLoginId(loginId);
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS);
    }

    /**
     * @apiNote 마이페이지 API
     * @implSpec request에서 토큰을 가져와 마이페이지의 정보를 볼 수 있게 한다
     * */
    @GetMapping("/my-page")
    public ResponseEntity<ResponseDto<MyPageResponse>> myPage(HttpServletRequest request) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                programmerService.myPage(JwtUtil.getId(request))
        );
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

    /**
     * @apiNote 모든 프로그래머의 정보를 읽는 API
     * */
    //@GetMapping
    public ResponseEntity<?> readAll() {
        return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.readAll());
    }

    /**
     * @apiNote 프로그래머 수정 API
     * @implSpec request 에서 받아온 정보와 요청한 정보가 동일해야 삭제할 수 있도록 구현함
     * @param programmerUpdateRequest 수정할 프로그래머의 정보가 있는 DTO 여기에 수정할 프로그래머의 ID도 있음
     * @param request 토큰을 받아올 헤더
     * */
    @PatchMapping
    public ResponseEntity<?> update(@RequestBody ProgrammerUpdateRequest programmerUpdateRequest, HttpServletRequest request) {
        programmerService.update(JwtUtil.getId(request), programmerUpdateRequest);

        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS
        );
    }

    /**
     * @implNote 프로그래머 삭제 API
     * @implSpec reqeust 에서 받아온 인증 정보와 요청한 정보가 동일해야 삭제할 수 있도록 구현함
     * @param request 토큰을 받아올 헤더
     * */
    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request) {
        programmerService.delete(JwtUtil.getId(request));
        return ResponseDto.toResponseEntity(ResponseMessage.NO_CONTENT);
    }
}