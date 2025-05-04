package com.codingMate.service.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.response.programmer.MyPageResponse;
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
    public MyPageResponse myPage(Long id) {
        Programmer result = queryFactory.selectFrom(programmer)
                .where(programmer.id.eq(id))
                .fetchOne();
        if(result == null) throw new NotFoundProgrammerException(id);

        return result.toMyPateDto();
    }
}