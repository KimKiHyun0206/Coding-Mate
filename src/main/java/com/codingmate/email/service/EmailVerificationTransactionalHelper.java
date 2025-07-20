package com.codingmate.email.service;

import com.codingmate.email.domain.EmailVerificationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class EmailVerificationTransactionalHelper {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsVerified(EmailVerificationToken entity) {
        log.debug("[EmailVerificationTransactionalHelper] markAsVerified({})", entity.getEmail());
        entity.markAsVerified();
        // 변경 사항은 이 트랜잭션에서 commit
        log.info("[EmailVerificationTransactionalHelper] 트랜잭션이 안전하게 커밋 되었습니다: email={}", entity.getEmail());
    }
}