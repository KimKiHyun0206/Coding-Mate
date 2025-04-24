package com.codingMate.dto.request.comment;

import com.codingMate.domain.comment.Comment;
import lombok.Data;

@Data
public class CommentCreateDto {
    private String content;
    private Long answerId;

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .recommendation(0)
                .build();
    }
}
