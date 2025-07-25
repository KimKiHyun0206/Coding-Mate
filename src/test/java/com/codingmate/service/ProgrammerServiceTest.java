package com.codingmate.service;

import com.codingmate.testutil.TestDataBuilder;
import com.codingmate.exception.exception.programmer.*;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.dto.request.ProgrammerUpdateRequest;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.programmer.service.ProgrammerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * {@code ProgrammerService}을 테스트하기 위한 테스트 클래스
 *
 * @see ProgrammerService
 * @author duskafka
 */
@SpringBootTest
@ActiveProfiles("test") // 테스트 프로파일 활성화 (application-test.yml 로드)
public class ProgrammerServiceTest {

    private final ProgrammerService programmerService;
    private final DefaultProgrammerRepository programmerRepository;

    @Autowired
    public ProgrammerServiceTest(ProgrammerService programmerService, DefaultProgrammerRepository programmerRepository) {
        this.programmerService = programmerService;
        this.programmerRepository = programmerRepository;
    }

    /**
     * <p>각 테스트 실행 전 데이터 클린업</p>
     * <p>모든 Programmer 데이터를 삭제하여 깨끗한 상태로 시작</p>
     */
    @BeforeEach
    void setUp() {
        programmerRepository.deleteAll();
    }


    /**
     * 예외가 발생하지 않는 테스트 케이스
     *
     * <li>처음 등록하는 것이기 때문에 에러가 발생하지 않는다.</li>
     */
    @Test
    void checkLoginIdAvailability_Success_WhenOtherIdNotExists() {
        String uniqueLoginId = "new_user_id";

        // 서비스 메서드 호출 시 예외가 발생하지 않음을 검증
        assertThatCode(() -> programmerService.checkLoginIdAvailability(uniqueLoginId))
                .doesNotThrowAnyException();
    }

    /**
     * 이미 존재하는 아이디를 체크하기 때문에 예외가 발생한다.
     */
    @Test
    void checkLoginIdAvailability_Fail_WhenOtherIdExists() {
        ProgrammerCreateRequest request = TestDataBuilder.createValidProgrammerCreateRequest();
        programmerRepository.save(Programmer.toEntity(
                request,
                new HashSet<>()
        ));

        // 서비스 메서드 호출 시 DuplicateProgrammerLoginIdException 예외가 발생하는지 검증
        assertThatThrownBy(() -> programmerService.checkLoginIdAvailability(request.loginId()))
                .isInstanceOf(DuplicateProgrammerLoginIdException.class);
    }

    /**
     * 데이터베이스에 다른 login_id가 등록되어 있지만 중복되지 않기 때문에 예외가 발생하지 않는 테스트.
     */
    @Test
    void checkLoginIdAvailability_Success_WhenOtherIdExists() {
        String notDuplicateLoginId = "not_duplicate_login_id";

        // 테스트를 위해 미리 데이터베이스에 ID를 등록 (실제 DB 연동)
        var existingProgrammer = Programmer.toEntity(
                TestDataBuilder.createValidProgrammerCreateRequest(),
                new HashSet<>()
        );

        programmerRepository.save(existingProgrammer); // 실제 DB에 저장

        // 서비스 메서드 호출 시 DuplicateProgrammerLoginIdException 예외가 발생하는지 검증
        assertThatCode(() -> programmerService.checkLoginIdAvailability(notDuplicateLoginId))
                .doesNotThrowAnyException();
    }


    /**
     * 마이페이지 조회 서비스 성공
     */
    @Test
    void getProgrammerMyPageInfo_Success() {
        ProgrammerCreateRequest request = TestDataBuilder.createValidProgrammerCreateRequest();
        programmerRepository.save(Programmer.toEntity(
                request,
                new HashSet<>()
        ));

        assertThatCode(() -> programmerService.getProgrammerMyPageInfo(request.loginId()))
                .doesNotThrowAnyException();
    }

    /**
     * 마이페이지 요청을 했지만 사용자를 찾을 수 없어서 예외가 발생함.
     */
    @Test
    void getProgrammerMyPageInfo_Fail_WhenUsernameNotMatch() {
        // 임시 저장될 엔티티
        programmerRepository.save(Programmer.toEntity(
                TestDataBuilder.createValidProgrammerCreateRequest(),
                new HashSet<>()
        ));

        assertThatThrownBy(() -> programmerService.getProgrammerMyPageInfo("not_match_login_id"))
                .isInstanceOf(NotFoundProgrammerException.class);
    }

    /**
     * 등록되지 않은 사용자라서 실패하는 테스트.
     */
    @Test
    void getProgrammerMyPageInfo_Fail_WhenProgrammerNotExist() {
        assertThatThrownBy(() -> programmerService.getProgrammerMyPageInfo("unknown"))
                .isInstanceOf(NotFoundProgrammerException.class);
    }

    /**
     * 수정에 성공하는 테스트
     */
    @Test
    void update_Success() {
        ProgrammerCreateRequest request = TestDataBuilder.createValidProgrammerCreateRequest();
        programmerRepository.save(Programmer.toEntity(
                request,
                new HashSet<>()
        ));

        assertThatCode(() -> programmerService.update(
                request.loginId(),
                new ProgrammerUpdateRequest(
                        "hong",
                        "홍길동",
                        "hong@naver.com",
                        "고을 사또 창고에 좋은 책 많아."
                )
        )).doesNotThrowAnyException();
    }

    /**
     * 사용자를 찾을 수 없어서 업데이트에 실패하는 테스트.
     * */
    @Test
    void update_Fail_WhenUsernameNotMatch() {
        programmerRepository.save(Programmer.toEntity(
                TestDataBuilder.createValidProgrammerCreateRequest(),
                new HashSet<>()
        ));

        assertThatCode(() -> programmerService.update(
                "not_match_login_id",
                new ProgrammerUpdateRequest(
                        "hong",
                        "홍길동",
                        "hong@naver.com",
                        "고을 사또 창고에 좋은 책 많아."
                )
        )).isInstanceOf(ProgrammerUpdateFailedException.class);
    }

    /**
     * 유효한 이메일 형식이 아니기에 업데이트에 실패하는 테스트.
     * */
    @Test
    void update_Fail_WhenInvalidEmail() {
        ProgrammerCreateRequest request = TestDataBuilder.createValidProgrammerCreateRequest();
        programmerRepository.save(Programmer.toEntity(
                request,
                new HashSet<>()
        ));

        var programmerUpdateRequest = new ProgrammerUpdateRequest(
                "hong",
                "I`m Hong!",
                "내 이메일은 비밀.",
                "고을 사또 창고에 좋은 책 많아."
        );

        assertThatCode(() -> programmerService.update(request.loginId(), programmerUpdateRequest))
                .isInstanceOf(InvalidEmailException.class);
    }

    /**
     * 유효한 이름 형식이 아니기 때문에 업데이트에 실패하는 테스트.
     * */
    @Test
    void update_Fail_WhenInvalidName() {
        ProgrammerCreateRequest request = TestDataBuilder.createValidProgrammerCreateRequest();
        programmerRepository.save(Programmer.toEntity(
                request,
                new HashSet<>()
        ));

        var programmerUpdateRequest = new ProgrammerUpdateRequest(
                "hong",
                "I`m Hong!",
                "hone@naver.com",
                "고을 사또 창고에 좋은 책 많아."
        );

        assertThatCode(() -> programmerService.update(request.loginId(), programmerUpdateRequest))
                .isInstanceOf(InvalidNameException.class);
    }

    /**
     * 사용자 계정 삭제에 성공하는 테스트.
     * */
    @Test
    void delete_Success() {
        String username = "new_login_id";
        programmerRepository.save(Programmer.toEntity(
                new ProgrammerCreateRequest(
                        username,
                        "githubId",
                        "qwoiequwoei1123123!",
                        "김기현",
                        "sample@naver.com"
                ),
                new HashSet<>()
        ));

        assertThatCode(() -> programmerService.delete(username))
                .doesNotThrowAnyException();
    }

    /**
     * 사용자 계정을 찾지 못하여 삭제에 실패하는 테스트.
     * */
    @Test
    void delete_Fail_WhenUsernameNotMatch(){
        programmerRepository.save(Programmer.toEntity(
                new ProgrammerCreateRequest(
                        "new_login_id",
                        "githubId",
                        "qwoiequwoei1123123!",
                        "김기현",
                        "sample@naver.com"
                ),
                new HashSet<>()
        ));

        assertThatCode(() -> programmerService.delete("not_match_login_id"))
                .isInstanceOf(NotFoundProgrammerException.class);
    }

    /**
     * 사용자가 존재하지 않아 계정을 삭제에 실패하는 테스트.
     * */
    @Test
    void delete_Fail_WhenProgrammerNotExist(){
        assertThatCode(() -> programmerService.delete("unknown_login_id"))
                .isInstanceOf(NotFoundProgrammerException.class);
    }
}