package com.codingMate.dto.response.programmer;

import com.codingMate.domain.programmer.Programmer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPageResponse {
    public String githubId;
    public String name;
    public String email;
    public Long numberOfAnswer;
    public String tip;

    @Override
    public String toString() {
        return "MyPateDto{" +
                "githubId='" + githubId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", numberOfAnswer=" + numberOfAnswer +
                ", tip='" + tip + '\'' +
                '}';
    }

    public static MyPageResponse from(Programmer programmer) {
        return MyPageResponse.builder()
                .githubId(programmer.getGithubId())
                .email(programmer.getEmail().getEmail())
                .name(programmer.getName().getName())
                .tip(programmer.getTip())
                .build();
    }
}