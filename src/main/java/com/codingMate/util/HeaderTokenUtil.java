package com.codingMate.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class HeaderTokenUtil {


    private static String header = "Coding-Mate-Auth";

    public static String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(header);
    }
}