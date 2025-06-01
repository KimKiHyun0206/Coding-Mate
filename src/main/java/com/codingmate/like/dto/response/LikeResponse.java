package com.codingmate.like.dto.response;

import lombok.Builder;

@Builder
public record LikeResponse(Integer likeCount) {
    public static LikeResponse of(Integer likeCount) {
        return LikeResponse.builder()
                .likeCount(likeCount)
                .build();
    }
}