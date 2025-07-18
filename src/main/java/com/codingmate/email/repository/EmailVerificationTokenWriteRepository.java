package com.codingmate.email.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codingmate.email.domain.QEmailVerificationToken.emailVerificationToken;

@Repository
@RequiredArgsConstructor
public class EmailVerificationTokenWriteRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public long verificationToken(Long id){
        return jpaQueryFactory.update(emailVerificationToken)
                .where(emailVerificationToken.id.eq(id))
                .set(emailVerificationToken.verified, true)
                .execute();
    }
}