package com.codingMate;

import com.codingMate.domain.answer.Answer;
import com.codingMate.dto.response.answer.AnswerListResponse;
import com.codingMate.dto.response.answer.QAnswerListDto;
import com.codingMate.repository.answer.DefaultAnswerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.codingMate.domain.answer.QAnswer.answer;
import static com.codingMate.domain.programmer.QProgrammer.programmer;

@Slf4j
@SpringBootTest
class AnswerReadTest {
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;
    @Autowired
    private DefaultAnswerRepository defaultAnswerRepository;

    @Autowired
    AnswerReadTest(EntityManager entityManager) {
        this.entityManager = entityManager;
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Test
    void readAnswerById() {
        Long id = 802L;
        Answer result = queryFactory.selectFrom(answer)
                .where(answer.id.eq(id))
                .join(answer.programmer).fetchJoin()
                .fetchOne();
        log.info("조회완료");
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void readAnswerByIdUserComment() {
        List<Answer> result = queryFactory.selectFrom(answer)
                .leftJoin(answer.programmer, programmer).fetchJoin()
                .fetch();
        log.info("조회완료");
        result.forEach(a -> {
            log.info("answer {} {} {} {} {}", a.getId(), a.getCode(), a.getBackJoonId(), a.getExplanation(), a.getLanguageType());
        });
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void read() {
        List<AnswerListResponse> list = queryFactory.select(new QAnswerListDto(answer.id, answer.backJoonId, answer.programmer.name.name))
                .from(answer)
                .join(answer.programmer)
                .fetch()
                .stream().toList();

        list.forEach(a -> {
            log.info("ANSWER LIST DTO {} {}", a.getBackjoonId(), a.getProgrammerName());
        });
    }
}