package com.codingMate.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class HeaderTokenUtil {

    @Value("${jwt.header}")
    private static String header;

    public static String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(header);
    }
}
