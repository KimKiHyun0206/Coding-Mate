package com.codingMate.service.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.util.SecurityUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codingMate.domain.programmer.QProgrammer.programmer;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final JPAQueryFactory queryFactory;

    /**
     * @implNote 아이디로 유저를 찾는다
     * */
    @Transactional(readOnly = true)
    public Programmer getUserWithAuthorities(String loginId) {
        log.info("getUserWithAuthorities({})", loginId);
        return queryFactory
                .selectFrom(programmer)
                .where(programmer.loginId.eq(loginId))
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public ProgrammerDto getMyUserWithAuthorities() {
        String currentUsername = String.valueOf(SecurityUtil.getCurrentUsername());
        return getUserWithAuthorities(currentUsername).toDto();
    }
}