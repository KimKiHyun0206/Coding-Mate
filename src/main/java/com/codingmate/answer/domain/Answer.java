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
@Table(name = "answer")
@Getter
@ToString(exclude = {"likes", "programmer"})
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @ManyToOne
    @JoinColumn(name = "programmer_id")
    private Programmer programmer;

    @Column(name = "vote_count")
    private Integer likeCount = 0;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    public static Answer toEntity(AnswerCreateRequest request, Programmer programmer) {
        return Answer.builder()
                .code(request.code())
                .title(request.title())
                .explanation(request.explanation())
                .languageType(request.languageType())
                .programmer(programmer)
                .backJoonId(request.backjoonId())
                .build();
    }

    public void upVote(Like like) {
        this.likeCount++;
        likes.add(like);
    }

    public void downVote(Like like) {
        if (this.likeCount > 0) {
            this.likeCount--;
            likes.remove(like);
        }
    }
}