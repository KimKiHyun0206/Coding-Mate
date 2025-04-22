package com.codingMate.domain.comment;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.programmer.Programmer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "recommendation")
    private Integer recommendation;

    @ManyToOne
    @JoinColumn(name = "answer_id", insertable = false, updatable = false)
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "programmer_id", insertable = false, updatable = false)
    private Programmer programmer;

    //@PostConstruct
    private void setDefaultRecommendation(){
        this.recommendation = 0;
    }
}
