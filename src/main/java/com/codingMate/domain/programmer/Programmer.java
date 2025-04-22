package com.codingMate.domain.programmer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.converter.PasswordEncodeConverter;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.domain.tip.Tip;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//TODO follow 기능 추가 염두해둘 것
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Programmer {
    @Id
    @GeneratedValue
    @Column(name = "programmer_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "github_link")
    private String githubLink;

    @Embedded
    @Column(name = "login_password")
    @Convert(converter = PasswordEncodeConverter.class)
    private String password;

    @Embedded
    @Column(name = "name")
    private Name name;

    @Embedded
    @Column(name = "email")
    private Email email;

    @OneToOne
    @JoinColumn(name = "tip_id")
    private Tip tip;

    @OneToMany(mappedBy = "programmer")
    private List<Answer> answers = new LinkedList<>();

    @OneToMany(mappedBy = "programmer")
    private List<Comment> comments = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "PROGRAMMER_TIP",
            joinColumns = @JoinColumn(name = "programmer_id"),
            inverseJoinColumns = @JoinColumn(name = "tip_id")
    )
    private Set<Tip> recommendationTips = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "PROGRAMMER_ANSWER",
            joinColumns = @JoinColumn(name = "programmer_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private Set<Answer> recommendationAnswers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "PROGRAMMER_COMMENT",
            joinColumns = @JoinColumn(name = "programmer_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> recommendationComments = new HashSet<>();

    @Builder
    public Programmer(String loginId, String githubLink, String password, Name name, Email email, Tip tip) {
        this.loginId = loginId;
        this.githubLink = githubLink;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tip = tip;
    }


    //TODO 추가되거나 삭제되는 비즈니스 로직 실행 시 크기가 변하지 않으면 예외를 발생시키도록 해야한다
    public void addRecommendationTip(Tip tip) {
        recommendationTips.add(tip);
    }

    public void addRecommendationAnswer(Answer answer) {
        recommendationAnswers.add(answer);
    }

    public void addRecommendationComment(Comment comment) {
        recommendationComments.add(comment);
    }

    public void removeRecommendationTip(Tip tip) {
        recommendationTips.remove(tip);
    }

    public void removeRecommendationAnswer(Answer answer) {
        recommendationAnswers.remove(answer);
    }

    public void removeRecommendationComment(Comment comment) {
        recommendationComments.remove(comment);
    }
}