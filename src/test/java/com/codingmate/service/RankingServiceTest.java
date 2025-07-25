package com.codingmate.service;

import com.codingmate.answer.service.AnswerService;
import com.codingmate.testutil.TestDataBuilder;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.ranking.service.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

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
        createProgrammerAndAnswer(1);
        createProgrammerAndAnswer(2);
        createProgrammerAndAnswer(3);
        createProgrammerAndAnswer(4);
        createProgrammerAndAnswer(5);
        createProgrammerAndAnswer(6);
        createProgrammerAndAnswer(7);
        createProgrammerAndAnswer(8);
        createProgrammerAndAnswer(9);
        createProgrammerAndAnswer(10);
    }

    private void createProgrammerAndAnswer(int numberOfAnswer) {
        var save = programmerRepository.save(Programmer.toEntity(
                TestDataBuilder.createValidProgrammerCreateRequest(),
                new HashSet<>())
        );

        for (int i = 0; i < numberOfAnswer; i++) {
            answerService.create(save.getLoginId(), TestDataBuilder.createValidAnswerCreateRequest());
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