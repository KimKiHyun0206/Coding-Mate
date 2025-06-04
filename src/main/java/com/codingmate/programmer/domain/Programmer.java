package com.codingmate.programmer.domain;

import com.codingmate.common.BaseEntity;
import com.codingmate.like.domain.Like;
import com.codingmate.programmer.domain.converter.PasswordEncodeConverter;
import com.codingmate.auth.domain.Authority;
import com.codingmate.programmer.domain.vo.Email;
import com.codingmate.programmer.domain.vo.Name;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


//TODO follow 기능 추가 염두해둘 것
@Entity
@Table(name = "programmer")
@Getter
@ToString(exclude = {"authority", "likedAnswers"})
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Programmer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "programmer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likedAnswers = new ArrayList<>();

    public static Programmer toEntity(ProgrammerCreateRequest request, Authority authority) {
        return Programmer.builder()
                .email(new Email(request.email()))
                .name(new Name(request.name()))
                .githubId(request.githubId())
                .password(request.password())
                .authority(authority)
                .loginId(request.loginId())
                .tip("팁이 있다면 공유해주세요")
                .build();
    }
}