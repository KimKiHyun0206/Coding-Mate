package com.codingMate.domain.programmer;

import com.codingMate.domain.programmer.converter.PasswordEncodeConverter;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.dto.response.programmer.SimpleProgrammerDto;
import jakarta.persistence.*;
import lombok.*;


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

    @Column(name = "number_of_answer")
    private Long numberOfAnswer = 0L;

    @Column(name = "tip", length = 2000)
    private String tip;

    @Builder
    public Programmer(String loginId, String githubLink, String password, Name name, Email email, Long numberOfAnswer, String tip) {
        this.loginId = loginId;
        this.githubLink = githubLink;
        this.password = password;
        this.name = name;
        this.email = email;
        this.numberOfAnswer = numberOfAnswer;
        this.tip = tip;
    }

    @Builder

    public ProgrammerDto toDto(){
        return new ProgrammerDto(
                id,
                loginId,
                githubLink,
                password,
                name.getName(),
                email.getEmail(),
                tip
        );
    }

    public SimpleProgrammerDto toSimpleDto(){
        return new SimpleProgrammerDto(id, name.getName());
    }


    public void addAnswer(){
        this.numberOfAnswer++;
    }
    public void removeAnswer(){
        this.numberOfAnswer--;
    }
}