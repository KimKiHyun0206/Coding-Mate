package com.codingmate.api.programmer;

import com.codingmate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.programmer.service.ProgrammerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * {@code /api/v1/programmer/login-id/exists GET}을 테스트하기 위한 테스트 클래스
 * */
@SpringBootTest
@ActiveProfiles("test") // 테스트 프로파일 활성화 (application-test.yml 로드)
public class LoginIdExistTest {

    // 실제 ProgrammerService 주입
    @Autowired
    private ProgrammerService programmerService;

    // 실제 DefaultProgrammerRepository 주입
    @Autowired
    private DefaultProgrammerRepository programmerRepository;

    /**
     * <p>각 테스트 실행 전 데이터 클린업</p>
     * <p>모든 Programmer 데이터를 삭제하여 깨끗한 상태로 시작</p>
     * */
    @BeforeEach
    void setUp() {
        programmerRepository.deleteAll();
    }

    /**
     * 예외가 발생하지 않는 테스트 케이스
     *
     * <li>처음 등록하는 것이기 때문에 에러가 발생하지 않는다.</li>
     * */
    @Test
    void successCase() {
        String uniqueLoginId = "new_user_id";

        // 서비스 메서드 호출 시 예외가 발생하지 않음을 검증
        assertThatCode(() -> programmerService.checkLoginIdAvailability(uniqueLoginId))
                .doesNotThrowAnyException();
    }

    /**
     * 이미 존재하는 아이디를 체크하기 때문에 예외가 발생한다.
     * */
    @Test
    void failCase() {
        String existingLoginId = "existing_user_id";

        // 테스트를 위해 미리 데이터베이스에 ID를 등록 (실제 DB 연동)
        Programmer existingProgrammer = Programmer.toEntity(
                new ProgrammerCreateRequest(
                        existingLoginId,
                        "githubId",
                        "qwoiequwoei1123123!",
                        "김기현",
                        "rlarlgus0206@naver.com"
                ),
                new HashSet<>()
        );

        programmerRepository.save(existingProgrammer); // 실제 DB에 저장

        // 서비스 메서드 호출 시 DuplicateProgrammerLoginIdException 예외가 발생하는지 검증
        assertThatThrownBy(() -> programmerService.checkLoginIdAvailability(existingLoginId))
                .isInstanceOf(DuplicateProgrammerLoginIdException.class)
                .hasMessage("요청한 로그인 아이디(existing_user_id)는 중복된 아이디입니다."); // 정확한 메시지 확인
    }

    /**
     * 데이터베이스에 다른 login_id가 등록되어 있지만 중복되지 않기 때문에 예외가 발생하지 않는 테스트.
     * */
    @Test
    void successCase2() {
        String loginId1 = "user_id_1";
        String loginId2 = "user_id_2";

        // 테스트를 위해 미리 데이터베이스에 ID를 등록 (실제 DB 연동)
        Programmer existingProgrammer = Programmer.toEntity(
                new ProgrammerCreateRequest(
                        loginId1,
                        "githubId",
                        "qwoiequwoei1123123!",
                        "김기현",
                        "rlarlgus0206@naver.com"
                ),
                new HashSet<>()
        );

        programmerRepository.save(existingProgrammer); // 실제 DB에 저장

        // 서비스 메서드 호출 시 DuplicateProgrammerLoginIdException 예외가 발생하는지 검증
        assertThatCode(() -> programmerService.checkLoginIdAvailability(loginId2))
                .doesNotThrowAnyException();
    }
}