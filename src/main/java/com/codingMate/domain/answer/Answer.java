package com.codingMate.domain.answer;

import com.codingMate.common.BaseEntity;
import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.domain.programmer.Programmer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(name = "backjoon_id")
    private Long backJoonId;

    @Column(name = "title")
    private String title;

    @Column(name = "answer")
    private String code;

    @Column(name = "explanation")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_type")
    private LanguageType languageType;

    @Setter
    @ManyToOne
    @JoinColumn(name = "programmer_id")
    private Programmer programmer;

    @Builder
    public Answer(Long backJoonId, String title, String code, String explanation, LanguageType languageType, Programmer programmer) {
        this.backJoonId = backJoonId;
        this.title = title;
        this.code = code;
        this.explanation = explanation;
        this.languageType = languageType;
        this.programmer = programmer;
    }
}