package com.codingMate.controller.api.programmer;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.dto.response.programmer.MyPageResponse;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.request.programmer.ProgrammerUpdateRequest;
import com.codingMate.exception.exception.jwt.UnMatchedAuthException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
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
    public ResponseEntity<?> create(@RequestBody ProgrammerCreateRequest dto) {
        log.info("create {}", dto.getLoginId());
        boolean isExistLoginId = programmerService.isExistLoginId(dto.getLoginId());
        if(!isExistLoginId){
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.create(dto));
        }else return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, "요청한 ID는 이미 존재하는 ID입니다");
    }

    /**
     * @apiNote 회원 생성 전에 아이디 중복 확인 버튼으로 아이디가 중복되는지 검사할 API
     * */
    @GetMapping("/login-id-exist")
    public ResponseEntity<?> isExistLoginId(@RequestParam("loginId") String loginId) {
        log.info("isExistLoginId {}", loginId);
        boolean isExistLoginId = programmerService.isExistLoginId(loginId);
        if(!isExistLoginId){
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, "존재하지 않는 ID입니다");
        }
        return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, "존재하는 ID입니다");
    }

    /**
     * @apiNote 마이페이지 API
     * @implSpec request에서 토큰을 가져와 마이페이지의 정보를 볼 수 있게 한다
     * */
    @GetMapping("/my-page")
    public ResponseEntity<?> myPage(HttpServletRequest request) {
        Long usernameFromToken = JwtUtil.getIdFromHttpServletRequest(request);
        MyPageResponse myPageResponse = programmerService.myPage(usernameFromToken);
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

    /**
     * @apiNote 모든 프로그래머의 정보를 읽는 API
     * */
    //@GetMapping
    public ResponseEntity<?> readAll() {
        log.info("readAll()");
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.readAll());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @apiNote 프로그래머 수정 API
     * @implSpec request 에서 받아온 정보와 요청한 정보가 동일해야 삭제할 수 있도록 구현함
     * @param programmerUpdateRequest 수정할 프로그래머의 정보가 있는 DTO 여기에 수정할 프로그래머의 ID도 있음
     * @param request 토큰을 받아올 헤더
     * */
    @PatchMapping
    public ResponseEntity<?> update(@RequestBody ProgrammerUpdateRequest programmerUpdateRequest, HttpServletRequest request) {
        try {
            Long idFromToken = JwtUtil.getIdFromHttpServletRequest(request);
            log.info("update({}, {})", idFromToken, programmerUpdateRequest.toString());
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.update(idFromToken, programmerUpdateRequest));
        } catch (UnMatchedAuthException unMatchedAuthException) {
            return ResponseDto.toResponseEntity(ResponseMessage.UNAUTHORIZED, unMatchedAuthException.getMessage());
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @implNote 프로그래머 삭제 API
     * @implSpec reqeust 에서 받아온 인증 정보와 요청한 정보가 동일해야 삭제할 수 있도록 구현함
     * @param request 토큰을 받아올 헤더
     * */
    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request) {
        try {
            Long idFromToken = JwtUtil.getIdFromHttpServletRequest(request);
            log.info("delete({})", idFromToken);
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, programmerService.delete(idFromToken));
        } catch (NotFoundProgrammerException notFoundProgrammerException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundProgrammerException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}