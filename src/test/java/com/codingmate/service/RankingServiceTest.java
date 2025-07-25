package com.codingmate.service;

import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.service.AnswerService;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.ranking.service.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

@Slf4j
@SpringBootTest
@ActiveProfiles("test") // 테스트 프로파일 활성화 (application-test.yml 로드)
public class RankingServiceTest {

    private final RankingService rankingService;
    private final DefaultProgrammerRepository programmerRepository;
    private final AnswerService answerService;

    @Autowired
    public RankingServiceTest(
            RankingService rankingService,
            DefaultProgrammerRepository programmerRepository,
            AnswerService answerService
    ) {
        this.rankingService = rankingService;
        this.programmerRepository = programmerRepository;
        this.answerService = answerService;
    }

    /**
     * 테스트 전 10명의 유저에게 각각 다른 풀이 수를 넣어주는 셋업
     */
    @BeforeEach
    public void setUp() {
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 1);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 2);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 3);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 4);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 5);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 6);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 7);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 8);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 9);
        createProgrammerAndAnswer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 10);
    }

    private void createProgrammerAndAnswer(String username, String email, int numberOfAnswer) {
        var save = programmerRepository.save(Programmer.toEntity(
                new ProgrammerCreateRequest(
                        username,
                        "github_id",
                        "qwpoeqdbkjl1231!",
                        "홍길동",
                        email + "@naver.com"
                ),
                new HashSet<>())
        );

        for (int i = 0; i < numberOfAnswer; i++) {
            var answerCreateRequest = new AnswerCreateRequest(
                    "code",
                    "title",
                    "explanation",
                    LanguageType.JAVA,
                    1L
            );
            answerService.create(save.getLoginId(), answerCreateRequest);
        }
    }

    /**
     * 데이터베이스에서 상위 10명의 랭킹을 읽어오는데 성공하는 테스트.
     */
    @Test
    void getRanking_Success() {
        assertThatCode(() -> {
            rankingService.getRanking().forEach(user -> {
                log.info("{} {} {}", user.name(), user.programmerId(), user.score());
            });
        }).doesNotThrowAnyException();
    }

    /**
     * 데이터베이스에서 읽어온 상위 10명의 랭킹을 읽어오는데 성공하고 Redis에 저장하는데 성공하는 테스트.
     */
    @Test
    void saveInRedis_Success() {
        assertThatCode(() -> rankingService.saveInRedis(rankingService.getRanking()))
                .doesNotThrowAnyException();
    }

    /**
     * Redis에서 상위 10명의 랭킹을 읽어오는데 성공하는 테스트.
     */
    @Test
    void getRankingFromRedis_Success() {
        assertThatCode(() -> {
            rankingService.saveInRedis(rankingService.getRanking());
            rankingService.getRankingFromRedis().forEach(user -> {
                log.info("{} {} {}", user.name(), user.programmerId(), user.score());
            });
        }).doesNotThrowAnyException();
    }
}