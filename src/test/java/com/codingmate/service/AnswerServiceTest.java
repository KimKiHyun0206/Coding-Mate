package com.codingmate.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.answer.service.AnswerService;
import com.codingmate.testutil.TestDataBuilder;
import com.codingmate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@SpringBootTest
@ActiveProfiles("test") // 테스트 프로파일 활성화 (application-test.yml 로드)
public class AnswerServiceTest {
    private final AnswerService answerService;
    private final DefaultProgrammerRepository programmerRepository;
    private final DefaultAnswerRepository answerRepository;

    private final String TEST_USERNAME = "new_login_id";
    private Long TEST_ANSWER_ID;

    @Autowired
    public AnswerServiceTest(AnswerService answerService, DefaultProgrammerRepository programmerRepository, DefaultAnswerRepository answerRepository) {
        this.answerService = answerService;
        this.programmerRepository = programmerRepository;
        this.answerRepository = answerRepository;
    }

    /**
     * 각 테스트 실행 전에 이전에 사용했던 Programmer와 Answer 엔티티를 삭제하고 Programmer와 Answer를 새로 등록한다.
     */
    @BeforeEach
    void setUp() {
        // 이전에 사용했던 엔티티 삭제
        answerRepository.deleteAll();
        programmerRepository.deleteAll();

        Programmer savedProgrammer = programmerRepository.save(
                Programmer.toEntity(TestDataBuilder.createValidProgrammerCreateRequest(), new HashSet<>())
        );

        Answer savedAnswer = answerRepository.save(
                Answer.toEntity(TestDataBuilder.createValidAnswerCreateRequest(), savedProgrammer)
        );

        // 테스트 메소드에서 사용할 ID를 인스턴스 변수에 저장
        this.TEST_ANSWER_ID = savedAnswer.getId();
    }

    /**
     * Answer 생성에 성공하는 테스트.
     */
    @Test
    void create_Success() {
        var answerCreateRequest = TestDataBuilder.createValidAnswerCreateRequest();

        assertThatCode(() -> answerService.create(TEST_USERNAME, answerCreateRequest))
                .doesNotThrowAnyException();
    }

    /**
     * username이 일치하지 않아 Answer 생성에 실패하는 테스트.
     */
    @Test
    void create_Fail_WhenUsernameNotMatch() {
        var answerCreateRequest = TestDataBuilder.createValidAnswerCreateRequest();
        assertThatCode(() -> answerService.create("not_match_username", answerCreateRequest))
                .isInstanceOf(NotFoundProgrammerException.class);
    }

    /**
     * Answer 조회에 성공하는 테스트.
     */
    @Test
    void read_Success() {
        assertThatCode(() -> answerService.read(TEST_ANSWER_ID, TEST_USERNAME))
                .doesNotThrowAnyException();
    }

    /**
     * answer_id가 존재하지 않아서 조회에 실패하는 테스트.
     */
    @Test
    void read_Fail_WhenAnswerIdNotExist() {
        assertThatCode(() -> answerService.read(TEST_ANSWER_ID + 1, TEST_USERNAME))
                .isInstanceOf(NotFoundAnswerException.class);
    }

    /**
     * 수정에 성공하는 테스트.
     */
    @Test
    void update_Success() {
        var answerUpdateRequest = TestDataBuilder.createValidAnswerUpdateRequest();

        assertThatCode(() -> answerService.update(TEST_USERNAME, TEST_ANSWER_ID, answerUpdateRequest))
                .doesNotThrowAnyException();
    }

    /**
     * answer_id가 존재하지 않아서 수정에 실패하는 테스트.
     */
    @Test
    void update_Fail_WhenAnswerIdNotExist() {
        var answerUpdateRequest = TestDataBuilder.createValidAnswerUpdateRequest();

        assertThatCode(() -> answerService.update(TEST_USERNAME, TEST_ANSWER_ID + 1, answerUpdateRequest))
                .isInstanceOf(NotFoundAnswerException.class);
    }

    /**
     * username이 일치하지 않아서 수정에 실패하는 테스트.
     */
    @Test
    void update_Fail_WhenUsernameNotMatch() {
        var answerUpdateRequest = TestDataBuilder.createValidAnswerUpdateRequest();

        assertThatCode(() -> answerService.update("not_match_login_id", TEST_ANSWER_ID, answerUpdateRequest))
                .isInstanceOf(AnswerAndProgrammerDoNotMatchException.class);
    }

    /**
     * Answer 삭제에 성공하는 테스트.
     */
    @Test
    void delete_Success() {
        assertThatCode(() -> answerService.delete(TEST_USERNAME, TEST_ANSWER_ID))
                .doesNotThrowAnyException();
    }

    /**
     * answer_id가 일치하지 않아 Answer 삭제에 실패하는 테스트.
     */
    @Test
    void delete_Fail_WhenAnswerIdNotExist() {
        assertThatCode(() -> answerService.delete(TEST_USERNAME, 2L))
                .isInstanceOf(NotFoundAnswerException.class);
    }

    /**
     * 작성자의 username이 일치하지 않아 Answer 삭제에 실패하는 테스트.
     */
    @Test
    void delete_Fail_WhenUsernameNotMatch() {
        assertThatCode(() -> answerService.delete("not_match_login_id", TEST_ANSWER_ID))
                .isInstanceOf(AnswerAndProgrammerDoNotMatchException.class);
    }
}