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
/**
 * 사용자에 대한 정보를 저장하기 위한 도메인.
 *
 * <ul>
 *     <li>id: Programmer의 PK</li>
 *     <li>loginId: 로그인에 필요한 id</li>
 *     <li>password: 로그인에 필요한 비밀번호(암호화되어 저장된다)</li>
 *     <li>name: 사용자의 이름</li>
 *     <li>email: 사용자의 이메일</li>
 *     <li>tip: 사용자가 작성한 자신만의 팁(기본값을 가짐)</li>
 *     <li>authority: 사용자가 가진 권한</li>
 *     <li>likedAnswers: 사용자가 좋아요를 누른 풀이</li>
 * </ul>
 *
 * @author duskakfa
 * @see Like
 * @see Email
 * @see Name
 * @see com.codingmate.programmer.domain.vo.Password
 * @see BaseEntity
 *
 * @since 1.0
 * */
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
    @JoinColumn(name = "authority")
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