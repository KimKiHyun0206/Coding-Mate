package com.codingmate.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.answer.service.AnswerService;
import com.codingmate.like.dto.response.LikeResponse;
import com.codingmate.like.repository.LikeRepository;
import com.codingmate.like.service.LikeService;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
@Transactional // 각 테스트 메서드가 끝날 때 트랜잭션을 롤백하여 데이터 일관성 유지
public class LikeServiceTest {
    private Long testAnswerId;

    private final ProgrammerCreateRequest createRequest = new ProgrammerCreateRequest(
            TEST_LOGIN_ID,
            TEST_GITHUB_ID,
            TEST_PASSWORD,
            TEST_NAME,
            TEST_EMAIL
    );

    private static final String TEST_LOGIN_ID = "test_login_id";
    private static final String TEST_GITHUB_ID = "test_github_id";
    private static final String TEST_PASSWORD = "testPassword123!!";
    private static final String TEST_NAME = "테스트사용자";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String ANSWER_CONTENT = "System.out.println(\"Hello\");";
    private static final String ANSWER_TITLE = "Hello World 예제";
    private static final String ANSWER_DESCRIPTION = "간단한 자바 코드입니다";
    private static final LanguageType ANSWER_LANGUAGE_TYPE = LanguageType.JAVA;


    private final LikeService likeService;
    private final DefaultAnswerRepository defaultAnswerRepository;
    private final DefaultProgrammerRepository programmerRepository;
    private final LikeRepository likeRepository;

    // 생성자 주입을 통해 의존성 명확히 선언
    @Autowired
    public LikeServiceTest(LikeService likeService,
                           DefaultAnswerRepository defaultAnswerRepository,
                           DefaultProgrammerRepository programmerRepository,
                           LikeRepository likeRepository

    ) {
        this.likeService = likeService;
        this.defaultAnswerRepository = defaultAnswerRepository;
        this.programmerRepository = programmerRepository;
        this.likeRepository = likeRepository;
    }

    @BeforeEach
    void setUp() {
        likeRepository.deleteAll();
        programmerRepository.deleteAll();
        Programmer programmer = programmerRepository.save(Programmer.toEntity(createRequest, new HashSet<>()));

        Answer entity = Answer.toEntity(new AnswerCreateRequest(
                ANSWER_CONTENT,
                ANSWER_TITLE,
                ANSWER_DESCRIPTION,
                ANSWER_LANGUAGE_TYPE,
                programmer.getId() // 이 부분이 중요: 저장된 programmer의 실제 ID 사용
        ), programmer);

        Answer save = defaultAnswerRepository.save(entity);

        this.testAnswerId = save.getId();
    }

    /**
     * 좋아요를 누르고 다시 취소하는 테스트.
     * */
    @Test
    void toggleLike_Success() {
        assertThatCode(() -> {
            LikeResponse like = likeService.toggleLike(TEST_LOGIN_ID, testAnswerId);
            assertThat(like).isNotNull();
            assertThat(like.likeCount()).isEqualTo(1);

            LikeResponse unLike = likeService.toggleLike(TEST_LOGIN_ID, testAnswerId);
            assertThat(unLike).isNotNull();
            assertThat(unLike.likeCount()).isEqualTo(0);
        }).doesNotThrowAnyException();
    }
}