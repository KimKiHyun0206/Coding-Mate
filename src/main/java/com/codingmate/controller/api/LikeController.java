package com.codingmate.controller.api;

import com.codingmate.common.response.ResponseDto;
import com.codingmate.common.response.ResponseMessage;
import com.codingmate.util.JwtUtil;
import com.codingmate.like.dto.response.LikeResponse;
import com.codingmate.like.service.LikeService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeController {
    private final LikeService likeService;

    @RolesAllowed("hasRole('ROLE_USER')")
    @PostMapping("/{answerId}")
    public ResponseEntity<ResponseDto<LikeResponse>> vote(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(name = "answerId") Long answerId
    ) {
        return ResponseDto.toResponseEntity(
                ResponseMessage.SUCCESS,
                likeService.toggleLike(userDetails.getUsername(), answerId)
        );
    }
}
