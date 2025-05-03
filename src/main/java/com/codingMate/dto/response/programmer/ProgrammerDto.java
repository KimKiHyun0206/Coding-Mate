package com.codingMate.dto.response.programmer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgrammerDto {
    private Long id;
    private String loginId;
    private String githubLink;
    private String password;
    private String name;
    private String email;
    private String tip;

    @Override
    public String toString() {
        return "ProgrammerDto{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", githubLink='" + githubLink + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}