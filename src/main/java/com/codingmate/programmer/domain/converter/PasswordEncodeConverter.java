package com.codingmate.programmer.domain.converter;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.programmer.domain.vo.Password;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Converter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "비밀번호 인코딩",
        detail = "인코딩에 BCryptPasswordEncoder 사용",  //클래스 분리 요함
        domain = "Programmer",
        lastReviewed = "2025.06.05"
)
public class PasswordEncodeConverter implements AttributeConverter<String, String> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        var password = new Password(attribute);
        password.setEncodePassword(passwordEncoder.encode(attribute));

        return password.getPassword();
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}