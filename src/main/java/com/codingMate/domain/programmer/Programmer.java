package com.codingMate.domain.programmer;

import com.codingMate.common.BaseEntity;
import com.codingMate.domain.programmer.converter.PasswordEncodeConverter;
import com.codingMate.domain.authority.Authority;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.dto.response.programmer.MyPageResponse;
import com.codingMate.dto.response.programmer.ProgrammerResponse;
import jakarta.persistence.*;
import lombok.*;


//TODO follow 기능 추가 염두해둘 것
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Programmer extends BaseEntity {
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
    @Column(name = "tip", length = 2000)
    private String tip;

    @ManyToOne
    private Authority authority;

    @Builder
    public Programmer(String loginId, String githubId, String password, Name name, Email email, String tip, Authority authority) {
        this.loginId = loginId;
        this.githubId = githubId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tip = tip;
        this.authority = authority;
    }


    public ProgrammerResponse toDto() {
        return new ProgrammerResponse(
                id,
                loginId,
                githubId,
                password,
                name.getName(),
                email.getEmail(),
                tip,
                authority.getAuthorityName()
        );
    }

    public MyPageResponse toMyPateDto() {
        return MyPageResponse.builder()
                .email(email.getEmail())
                .githubId(githubId)
                .name(name.getName())
                .numberOfAnswer(0L)
                .tip(tip)
                .build();
    }
}