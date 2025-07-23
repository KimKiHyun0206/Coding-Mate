package com.codingmate.api.programmer;

import com.codingmate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.programmer.service.ProgrammerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 테스트 프로파일 활성화 (application-test.yml 로드)
public class LoginIdExistTest {

    @Autowired
    private MockMvc mockMvc;

    // ProgrammerService를 Mocking하여 실제 DB 접근 없이 테스트
    // 실제 서비스가 아닌 가짜 서비스를 주입받아 사용합니다.
    @Autowired // 스프링 컨텍스트에 Mock 객체를 등록
    private ProgrammerService programmerService;

    @Autowired
    private DefaultProgrammerRepository programmerRepository; // 실제 리포지토리 빈 주입 (테스트 데이터 준비용)

    @BeforeEach
    void setUp() {
        // 각 테스트 실행 전 데이터를 클린업 (Transactional과 함께 사용 시 주로 필요 없음)
        // 하지만 혹시 모를 상황에 대비하거나, 특정 초기 데이터가 필요한 경우 여기에 추가
        programmerRepository.deleteAll(); // 모든 Programmer 데이터를 삭제하여 깨끗한 상태로 시작
    }


    @Test
    @DisplayName("존재하지 않는 ID인 경우, 중복 확인 성공 (예외 발생 안 함)")
    void successCase() {
        String uniqueLoginId = "new_user_id";

        // 서비스 메서드 호출 시 예외가 발생하지 않음을 검증
        assertThatCode(() -> programmerService.checkLoginIdAvailability(uniqueLoginId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 존재하는 ID인 경우, 중복 확인 실패 (DuplicateProgrammerLoginIdException 발생)")
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

        log.info("저장 완료 {}", existingProgrammer.getLoginId());

        // 서비스 메서드 호출 시 DuplicateProgrammerLoginIdException 예외가 발생하는지 검증
        assertThatThrownBy(() -> programmerService.checkLoginIdAvailability(existingLoginId))
                .isInstanceOf(DuplicateProgrammerLoginIdException.class)
                .hasMessage("요청한 로그인 아이디(existing_user_id)는 중복된 아이디입니다."); // 정확한 메시지 확인
    }
}