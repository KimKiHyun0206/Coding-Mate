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


/**
 * 백준 문제에 대한 사용자 답변을 나타내는 도메인 엔티티.
 *
 * <ul>
 *     <li>id: Answer의 PK</li>
 *     <li>backJoonId: 백준 문제의 번호</li>
 *     <li>title: 사용자가 작성한 풀이 제목</li>
 *     <li>code: 사용자가 작성한 풀이</li>
 *     <li>explanation: 사용자가 작성한 풀이에 대한 설명</li>
 *     <li>languageType: 사용자가 작성한 풀이의 언어</li>
 *     <li>programmer: 작성한 사용자</li>
 *     <li>likeCount: 좋아요를 받은 수</li>
 *     <li>likes: 좋아요를 누른 사람에 대한 정보</li>
 * </ul>
 *
 * @author duskafka
 * @see Like
 * @see Programmer
 * @see BaseEntity
 * @since 1.0
 * */
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
                .likeCount(0)
                .likes(new HashSet<>())
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