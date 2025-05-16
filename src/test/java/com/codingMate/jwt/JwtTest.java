package com.codingMate.jwt;
import com.codingMate.exception.exception.jwt.ExpiredTokenException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class) // TokenProvider가 스프링 빈인 경우
public class JwtTest {

    @Autowired
    private TokenProvider tokenProvider;



    @Test
    @DisplayName("유효기간이 지난 토큰에 대한 예외를 발생시키는 테스트")
    public void expiredTokenTest() {
        //언제 것일지 모르는 오래된 토큰
        String effectivePeriodToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxcXEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiaWQiOjE5MDIsImV4cCI6MTc0NzAzMjMxMH0.plvPRjkKzG43pJbxl0JlLTKh0ufFCI8NcntDiVLPqq37E_MnJzxXzuQKtNzTCsy6T44rwm7QdCQSaU2qMu9byA";
        Assertions.assertThrows(ExpiredTokenException.class, () -> tokenProvider.validateToken(effectivePeriodToken));
    }
}