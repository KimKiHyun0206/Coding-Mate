package com.codingMate.dto.request.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import lombok.Data;

@Data
public class ProgrammerCreateRequest {
    private String loginId;
    private String githubId;
    private String password;
    private String name;
    private String email;

    public Programmer toEntity(){
        return Programmer.builder()
                .loginId(loginId)
                .githubId(githubId)
                .password(password)
                .name(new Name(name))
                .email(new Email(email))
                .tip("팁이 있다면 공유해주세요")
                .numberOfAnswer(0L)
                .build();
    }
}
