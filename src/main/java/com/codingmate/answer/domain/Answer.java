package com.codingmate.answer.domain;

import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.common.BaseEntity;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.like.domain.Like;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString
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

    @Column(name = "vote_count")
    private Integer likeCount = 0;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Answer(Long backJoonId, String title, String code, String explanation, LanguageType languageType, Programmer programmer) {
        this.backJoonId = backJoonId;
        this.title = title;
        this.code = code;
        this.explanation = explanation;
        this.languageType = languageType;
        this.programmer = programmer;
    }

    public static Answer toEntity(AnswerCreateRequest request,Programmer programmer){
        return Answer.builder()
                .code(request.code())
                .title(request.title())
                .explanation(request.explanation())
                .languageType(request.languageType())
                .programmer(programmer)
                .backJoonId(request.backjoonId())
                .build();
    }

    public void upVote(Like like){
        this.likeCount++;
        likes.add(like);
    }

    public void downVote(Like like){
        if(this.likeCount > 0){
            this.likeCount--;
            likes.remove(like);
        }
    }
}