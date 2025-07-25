package com.codingmate.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.answer.service.AnswerService;
import com.codingmate.like.dto.response.LikeResponse;
import com.codingmate.like.service.LikeService;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private DefaultAnswerRepository defaultAnswerRepository;

    @Autowired
    private DefaultProgrammerRepository programmerRepository;

    private Long testAnswerId;

    private final ProgrammerCreateRequest createRequest = new ProgrammerCreateRequest(
            "new_login_id",
            "github_id",
            "qwlkekd123!!!@#",
            "홍길동",
            "hong@gmail.com"
    );

    @BeforeEach
    void setUp() {
        programmerRepository.deleteAll();
        Programmer programmer = programmerRepository.save(Programmer.toEntity(createRequest, new HashSet<>()));

        Answer entity = Answer.toEntity(new AnswerCreateRequest(
                "System.out.println(\"Hello\");",
                "Hello World 예제",
                "간단한 자바 코드입니다",
                LanguageType.JAVA,
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
            LikeResponse like = likeService.toggleLike(createRequest.loginId(), testAnswerId);
            assertThat(like).isNotNull();
            assertThat(like.likeCount()).isEqualTo(1);

            LikeResponse unLike = likeService.toggleLike(createRequest.loginId(), testAnswerId);
            assertThat(unLike).isNotNull();
            assertThat(unLike.likeCount()).isEqualTo(0);
        }).doesNotThrowAnyException();
    }
}