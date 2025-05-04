package com.codingMate.dto.response.programmer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
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
}