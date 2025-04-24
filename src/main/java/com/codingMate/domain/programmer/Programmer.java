package com.codingMate.domain.programmer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.converter.PasswordEncodeConverter;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.domain.tip.Tip;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

//TODO follow 기능 추가 염두해둘 것
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Programmer {
    @Id
    @GeneratedValue
    @Column(name = "programmer_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "github_link")
    private String githubLink;

    @Column(name = "login_password")
    @Convert(converter = PasswordEncodeConverter.class)
    private String password;

    @Column(name = "name")
    private Name name;

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
    private List<Tip> recommendationTips = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "PROGRAMMER_ANSWER",
            joinColumns = @JoinColumn(name = "programmer_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private List<Answer> recommendationAnswers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "PROGRAMMER_COMMENT",
            joinColumns = @JoinColumn(name = "programmer_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private List<Comment> recommendationComments = new ArrayList<>();

    @Builder
    public Programmer(String loginId, String githubLink, String password, Name name, Email email, Tip tip) {
        this.loginId = loginId;
        this.githubLink = githubLink;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tip = tip;
    }

    public ProgrammerDto toDto(){
        return new ProgrammerDto(
                id,
                loginId,
                githubLink,
                password,
                name.getName(),
                email.getEmail(),
                tip.toDto(),
                answers.stream().map(Answer::toDto).toList(),
                comments.stream().map(Comment::toDto).toList(),
                recommendationTips.stream().map(Tip::toDto).toList(),
                recommendationAnswers.stream().map(Answer::toDto).toList(),
                recommendationComments.stream().map(Comment::toDto).toList()
        );
    }
}