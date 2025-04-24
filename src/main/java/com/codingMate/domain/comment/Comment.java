package com.codingMate.domain.comment;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.response.comment.CommentDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment")
    private String content;

    @Column(name = "recommendation")
    private Integer recommendation;

    @ManyToOne
    @Setter
    @JoinColumn(name = "answer_id", insertable = false, updatable = false)
    private Answer answer;

    @ManyToOne
    @Setter
    @JoinColumn(name = "programmer_id", insertable = false, updatable = false)
    private Programmer programmer;

    @Builder
    public Comment(String content, Integer recommendation, Answer answer, Programmer programmer) {
        this.content = content;
        this.recommendation = recommendation;
        this.answer = answer;
        this.programmer = programmer;
    }

    public CommentDto toDto() {
        return new CommentDto(id, content, recommendation, answer.toDto(), programmer.toDto());
    }

    public void recommend() {
        this.recommendation++;
    }

    public void nonRecommend() {
        this.recommendation--;
    }
}
