package com.codingMate.domain.answer;

import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.response.answer.AnswerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue
    @Column(name = "answer_id")
    private Long id;

    @Column(name = "backjoon_id")
    private Long backJoonId;

    @Column(name = "answer")
    private String code;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "recommendation")
    private Integer recommendation = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_type")
    private LanguageType languageType;

    @Setter
    @ManyToOne
    @JoinColumn(name = "programmer_id")
    private Programmer programmer;

    @Builder
    public Answer(Long backJoonId, String code, String explanation, Integer recommendation, LanguageType languageType, Programmer programmer) {
        this.backJoonId = backJoonId;
        this.code = code;
        this.explanation = explanation;
        this.recommendation = recommendation;
        this.languageType = languageType;
        this.programmer = programmer;
    }

    public AnswerDto toDto(){
        return new AnswerDto(
                id,
                backJoonId,
                code,
                explanation,
                recommendation,
                programmer.toDto(),
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
