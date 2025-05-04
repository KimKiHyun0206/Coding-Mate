package com.codingMate.domain.programmer;

import com.codingMate.domain.programmer.converter.PasswordEncodeConverter;
import com.codingMate.domain.programmer.vo.Authority;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.dto.response.programmer.MyPageResponse;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.dto.response.programmer.SimpleProgrammerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


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

    @Column(name = "login_id", unique = true)
    private String loginId;

    @Column(name = "github_id")
    private String githubId;

    @Column(name = "login_password")
    @Convert(converter = PasswordEncodeConverter.class)
    private String password;

    @Column(name = "name")
    private Name name;

    @Column(name = "email")
    private Email email;

    @Column(name = "number_of_answer")
    private Long numberOfAnswer = 0L;

    @Column(name = "tip", length = 2000)
    private String tip;

    @ManyToMany
    @JoinTable(
            name = "programmer_authority",
            joinColumns = {@JoinColumn(name = "programmer_id", referencedColumnName = "programmer_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;

    @Builder
    public Programmer(String loginId, String githubId, String password, Name name, Email email, Long numberOfAnswer, String tip, Set<Authority> authorities) {
        this.loginId = loginId;
        this.githubId = githubId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.numberOfAnswer = numberOfAnswer;
        this.tip = tip;
        this.authorities = authorities;
    }


    public ProgrammerDto toDto(){
        return new ProgrammerDto(
                id,
                loginId,
                githubId,
                password,
                name.getName(),
                email.getEmail(),
                tip
        );
    }

    public SimpleProgrammerDto toSimpleDto(){
        return new SimpleProgrammerDto(id, name.getName());
    }

    public MyPageResponse toMyPateDto(){
        return MyPageResponse.builder()
                .email(email.getEmail())
                .githubId(githubId)
                .name(name.getName())
                .numberOfAnswer(numberOfAnswer)
                .tip(tip)
                .build();
    }


    public void addAnswer(){
        this.numberOfAnswer++;
    }
    public void removeAnswer(){
        this.numberOfAnswer--;
    }
}