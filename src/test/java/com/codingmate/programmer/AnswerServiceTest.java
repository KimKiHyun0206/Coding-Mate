package com.codingmate.programmer;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.answer.service.AnswerService;
import com.codingmate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
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

    @Autowired
    private AnswerService answerService;

    @Autowired
    private DefaultProgrammerRepository programmerRepository;

    @Autowired
    private DefaultAnswerRepository answerRepository;

    private final String username = "new_login_id";
    private Long testAnswerId;

    /**
     * 각 테스트 실행 전에 이전에 사용했던 Programmer와 Answer 엔티티를 삭제하고 Programmer와 Answer를 새로 등록한다.
     */
    @BeforeEach
    void setUp() {
        // 이전에 사용했던 엔티티 삭제
        answerRepository.deleteAll();
        programmerRepository.deleteAll();

        // 새로운 Programmer 등록 (실제 저장된 객체 받기)
        var programmerCreateRequest = new ProgrammerCreateRequest(
                username,
                "sample_github_id",
                "qweoi12311e23!",
                "홍길동",
                "hong@naver.com"
        );
        // save 후 반환되는 programmer 객체를 사용
        Programmer savedProgrammer = programmerRepository.save(Programmer.toEntity(programmerCreateRequest, new HashSet<>()));


        // Answer 저장 (실제 저장된 객체 받기)
        Answer entity = Answer.toEntity(new AnswerCreateRequest(
                "System.out.println(\"Hello\");",
                "Hello World 예제",
                "간단한 자바 코드입니다",
                LanguageType.JAVA,
                savedProgrammer.getId() // 이 부분이 중요: 저장된 programmer의 실제 ID 사용
        ), savedProgrammer);

        // save 후 반환되는 answer 객체를 사용
        Answer savedAnswer = answerRepository.save(entity);

        // 테스트 메소드에서 사용할 ID를 인스턴스 변수에 저장
        this.testAnswerId = savedAnswer.getId(); // setUp 메소드 상단에 private Long testAnswerId; 선언
    }

    /**
     * Answer 생성에 성공하는 테스트.
     */
    @Test
    void create_Success() {
        var answerCreateRequest = new AnswerCreateRequest(
                "System.out.println(\"Hello\");",
                "Hello World 예제",
                "간단한 자바 코드입니다",
                LanguageType.JAVA,
                1L
        );

        assertThatCode(() -> answerService.create(username, answerCreateRequest))
                .doesNotThrowAnyException();
    }

    /**
     * username이 일치하지 않아 Answer 생성에 실패하는 테스트.
     */
    @Test
    void create_Fail_WhenUsernameNotMatch() {
        var answerCreateRequest = new AnswerCreateRequest(
                "System.out.println(\"Hello\");",
                "Hello World 예제",
                "간단한 자바 코드입니다",
                LanguageType.JAVA,
                1L
        );

        assertThatCode(() -> answerService.create("not_match_username", answerCreateRequest))
                .isInstanceOf(NotFoundProgrammerException.class);
    }

    /**
     * Answer 조회에 성공하는 테스트.
     */
    @Test
    void read_Success() {
        assertThatCode(() -> answerService.read(testAnswerId, username))
                .doesNotThrowAnyException();
    }

    /**
     * answer_id가 존재하지 않아서 조회에 실패하는 테스트.
     */
    @Test
    void read_Fail_WhenAnswerIdNotExist() {
        assertThatCode(() -> answerService.read(testAnswerId + 1, username))
                .isInstanceOf(NotFoundAnswerException.class);
    }

    /**
     * 수정에 성공하는 테스트.
     */
    @Test
    void update_Success() {
        var answerUpdateRequest = new AnswerUpdateRequest(
                "Code",
                "Title",
                "Explanation",
                LanguageType.JAVA,
                2L
        );

        assertThatCode(() -> answerService.update(username, testAnswerId, answerUpdateRequest))
                .doesNotThrowAnyException();
    }

    /**
     * answer_id가 존재하지 않아서 수정에 실패하는 테스트.
     */
    @Test
    void update_Fail_WhenAnswerIdNotExist() {
        var answerUpdateRequest = new AnswerUpdateRequest(
                "Code",
                "Title",
                "Explanation",
                LanguageType.JAVA,
                2L
        );

        assertThatCode(() -> answerService.update(username, testAnswerId + 1, answerUpdateRequest))
                .isInstanceOf(NotFoundAnswerException.class);
    }

    /**
     * username이 일치하지 않아서 수정에 실패하는 테스트.
     */
    @Test
    void update_Fail_WhenUsernameNotMatch() {
        var answerUpdateRequest = new AnswerUpdateRequest(
                "Code",
                "Title",
                "Explanation",
                LanguageType.JAVA,
                2L
        );

        assertThatCode(() -> answerService.update("not_match_login_id", testAnswerId, answerUpdateRequest))
                .isInstanceOf(AnswerAndProgrammerDoNotMatchException.class);
    }

    /**
     * Answer 삭제에 성공하는 테스트.
     */
    @Test
    void delete_Success() {
        assertThatCode(() -> answerService.delete(username, testAnswerId))
                .doesNotThrowAnyException();
    }

    /**
     * answer_id가 일치하지 않아 Answer 삭제에 실패하는 테스트.
     */
    @Test
    void delete_Fail_WhenAnswerIdNotExist() {
        assertThatCode(() -> answerService.delete(username, 2L))
                .isInstanceOf(NotFoundAnswerException.class);
    }

    /**
     * 작성자의 username이 일치하지 않아 Answer 삭제에 실패하는 테스트.
     */
    @Test
    void delete_Fail_WhenUsernameNotMatch() {
        assertThatCode(() -> answerService.delete("not_match_login_id", testAnswerId))
                .isInstanceOf(AnswerAndProgrammerDoNotMatchException.class);
    }
}