package com.codingMate.domain.programmer.converter;

import com.codingMate.domain.programmer.vo.Password;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Converter
@RequiredArgsConstructor
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