package com.codingMate.controller.api.tip;

import com.codingMate.common.response.ResponseDto;
import com.codingMate.common.response.ResponseMessage;
import com.codingMate.exception.exception.tip.NotFoundTipException;
import com.codingMate.service.tip.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tip")
@RequiredArgsConstructor
public class TipController {
    private final TipService tipService;

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, tipService.read(id));
        } catch (NotFoundTipException notFoundTipException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundTipException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/recommend/{id}")
    public ResponseEntity<?> recommend(@PathVariable Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, tipService.recommend(id));
        } catch (NotFoundTipException notFoundTipException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundTipException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/non-recommend/{id}")
    public ResponseEntity<?> nonRecommend(@PathVariable Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, tipService.nonRecommend(id));
        } catch (NotFoundTipException notFoundTipException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundTipException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            return ResponseDto.toResponseEntity(ResponseMessage.SUCCESS, tipService.read(id));
        } catch (NotFoundTipException notFoundTipException) {
            return ResponseDto.toResponseEntity(ResponseMessage.BAD_REQUEST, notFoundTipException.getMessage());
        } catch (Exception e) {
            return ResponseDto.toResponseEntity(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}