package com.codingMate.domain.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.answer.AnswerDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue
    @Column(name = "answer_id")
    private Long id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "recommendation")
    private Integer recommendation;

    @ManyToOne
    @JoinColumn(name = "programmer_id", insertable = false, updatable = false)
    private Programmer programmer;

    @OneToMany(mappedBy = "answer")
    private List<Comment> comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_type")
    private LanguageType languageType;

    public AnswerDto toDto(){
        return new AnswerDto(
                id,
                answer,
                explanation,
                recommendation,
                programmer.toDto(),
                comment.stream().map(Comment::toDto).toList(),
                languageType
        );
    }

    public void recommend(){
        this.recommendation++;
    }

    public void nonRecommend(){
        this.recommendation--;
    }
}
