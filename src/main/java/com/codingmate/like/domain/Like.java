package com.codingmate.like.domain;

import com.codingmate.answer.domain.Answer;
import com.codingmate.common.BaseEntity;
import com.codingmate.programmer.domain.Programmer;
import jakarta.persistence.*;
import lombok.*;

/**
 * 좋아요를 누른 기록을 관리하기 위한 도메인
 *
 * <li>id: Like의 PK</li>
 * <li>programmer: 좋아요를 누른 사용자</li>
 * <li>answer: 좋아요가 등록된 문제</li>
 *
 * @author duskafka
 * @see Programmer
 * @see Answer
 * */
@Entity
@Getter
@ToString(exclude = {"programmer", "answer"})
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answer_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"programmer_id", "answer_id"})
})
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programmer_id", nullable = false)
    private Programmer programmer;  //좋아요를 누른 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    public static Like toEntity(Programmer programmer, Answer answer) {
        if (programmer == null && answer == null) {
            throw new IllegalArgumentException("Programmer와 Answer는 null이 될 수 없습니다");
        }

        return Like.builder()
                .programmer(programmer)
                .answer(answer)
                .build();
    }
}