package com.codingmate.like.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "좋아요(추천) 수를 응답하는 DTO")
public record LikeResponse(
        @Schema(description = "좋아요(추천) 수", example = "50")
        Integer likeCount
) {
    /**
     * 좋아요(추천) 수를 사용하여 LikeResponse를 생성합니다.
     *
     * @param likeCount 좋아요(추천) 수
     * @return LikeResponse 객체
     */
    public static LikeResponse of(Integer likeCount) {
        return LikeResponse.builder()
                .likeCount(likeCount)
                .build();
    }
}