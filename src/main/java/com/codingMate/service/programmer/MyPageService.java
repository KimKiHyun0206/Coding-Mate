package com.codingMate.service.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.programmer.MyPateDto;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codingMate.domain.programmer.QProgrammer.programmer;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public MyPateDto myPage(String loginId) {
        Programmer result = queryFactory.selectFrom(programmer)
                .where(programmer.loginId.eq(loginId))
                .fetchOne();
        if(result == null) throw new NotFoundProgrammerException(loginId);

        return result.toMyPateDto();
    }
}