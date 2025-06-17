package com.codingmate.programmer.service;

import com.codingmate.answer.repository.AnswerReadRepository;
import com.codingmate.answer.repository.AnswerWriteRepository;
import com.codingmate.auth.service.AuthorityFinder;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.dto.request.ProgrammerUpdateRequest;
import com.codingmate.programmer.dto.response.MyPageResponse;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.exception.exception.programmer.ProgrammerNotCreateException;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.programmer.repository.ProgrammerReadRepository;
import com.codingmate.programmer.repository.ProgrammerWriteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerService {
    private final DefaultProgrammerRepository defaultProgrammerRepository;
    private final ProgrammerWriteRepository writeRepository;
    private final ProgrammerReadRepository readRepository;
    private final AnswerReadRepository answerReadRepository;
    private final AnswerWriteRepository answerWriteRepository;
    private final AuthorityFinder authorityFinder;

    /**
     * 새로운 프로그래머 계정을 생성합니다.
     * 계정 생성 전에 로그인 ID의 중복 여부를 확인합니다.
     *
     * @param request 생성할 프로그래머의 정보를 담은 DTO
     * @throws com.codingmate.exception.exception.programmer.DuplicateProgrammerLoginIdException 요청한 {@code loginId}가 이미 존재할 경우 발생합니다.
     * @throws com.codingmate.exception.exception.programmer.ProgrammerNotCreateException 프로그래머 생성 중 예상치 못한 오류가 발생하여 계정이 생성되지 않았을 경우 발생합니다.
     */
    @Transactional
    public void create(ProgrammerCreateRequest request) {
        log.info("[ProgrammerService] create({})", request.loginId());

        log.debug("[ProgrammerService] Create request details: {}", request);
        if (readRepository.isExistLoginId(request.loginId())) {
            log.warn("[ProgrammerService] Create failed: Duplicate login ID detected. loginId: {}", request.loginId());
            throw new DuplicateProgrammerLoginIdException(
                    ErrorMessage.DUPLICATE_PROGRAMMER,
                    "요청한 Id는 이미 존재하는 Id입니다"
            );
        }
        log.debug("[ProgrammerService] Login ID {} is available.", request.loginId());


        writeRepository.create(
                request,
                authorityFinder.getUserAuthority("ROLE_USER")
        );

        log.info("[ProgrammerService] Programmer created successfully with loginId: {}", request.loginId());
    }

    /**
     * 주어진 로그인 ID의 사용 가능 여부를 확인합니다.
     *
     * @param loginId 확인할 로그인 ID
     * @throws com.codingmate.exception.exception.programmer.DuplicateProgrammerLoginIdException 요청한 {@code loginId}가 이미 존재하여 사용할 수 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public void checkLoginIdAvailability(String loginId) {
        log.info("[ProgrammerService] checkLoginIdAvailability({})", loginId);

        log.debug("[ProgrammerService] Attempting to find existing loginId: {}", loginId);
        if (readRepository.isExistLoginId(loginId)) {
            log.warn("[ProgrammerService] Login ID {} is already taken.", loginId);
            throw new DuplicateProgrammerLoginIdException(
                    ErrorMessage.DUPLICATE_PROGRAMMER,
                    "요청한 ID는 중복된 ID입니다. " + loginId
            );
        }
        log.info("[ProgrammerService] Login ID {} is available.", loginId);
    }

    /**
     * 특정 프로그래머의 마이페이지 정보를 조회합니다.
     *
     * @param programmerId 조회할 프로그래머의 ID
     * @return 프로그래머의 마이페이지 정보를 담은 응답 DTO
     * @throws com.codingmate.exception.exception.programmer.NotFoundProgrammerException 지정된 {@code programmerId}를 가진 프로그래머를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public MyPageResponse getProgrammerMyPageInfo(Long programmerId) {
        log.info("[ProgrammerService] getProgrammerMyPageInfo({})", programmerId);

        log.debug("[ProgrammerService] Searching for programmer with ID: {}", programmerId);
        var programmer = defaultProgrammerRepository.findById(programmerId)
                .orElseThrow(() -> {
                    log.warn("[ProgrammerService] MyPage info failed: Programmer not found with ID: {}", programmerId);
                    return new NotFoundProgrammerException(
                            ErrorMessage.INVALID_ID,
                            String.format("%d는 존재하지 않는 ProgrammerID입니다.", programmerId));
                });
        log.debug("[ProgrammerService] Programmer found: {}. Counting answers...", programmerId);

        long writtenAnswersCount = answerReadRepository.countProgrammerWroteAnswer(programmerId);
        log.debug("[ProgrammerService] Programmer {} has written {} answers.", programmerId, writtenAnswersCount);
        log.info("[ProgrammerService] Successfully retrieved MyPage info for programmerId: {}", programmerId);

        return MyPageResponse.of(programmer, writtenAnswersCount);
    }

    @Transactional(readOnly = true)
    public String getProgrammerRole(Long id) {
        log.info("[ProgrammerService] getProgrammerRole({})", id);

        log.debug("[ProgrammerService] Searching for programmer with ID: {}", id);
        return readRepository.readProgrammerRole(id)
                .orElseThrow(() -> {
                    log.warn("[ProgrammerService] Programmer not found by ID: {}", id);
                    return new NotFoundProgrammerException(
                            ErrorMessage.NOT_FOUND_PROGRAMMER,
                            String.format("ID '%d'를 가진 Programmer를 찾을 수 없어 업데이트를 진행할 수 없습니다.", id)
                    );
                });
    }

    /**
     * 특정 프로그래머의 정보를 업데이트합니다.
     *
     * @param programmerId 업데이트할 프로그래머의 ID
     * @param request      업데이트할 내용을 담은 DTO
     * @throws com.codingmate.exception.exception.programmer.NotFoundProgrammerException 지정된 {@code programmerId}를 가진 프로그래머를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional
    public void update(Long programmerId, ProgrammerUpdateRequest request) {
        log.info("[ProgrammerService] update({}, {})", programmerId, request);

        log.debug("[ProgrammerService] Update request details for programmerId {}: {}", programmerId, request);

        long changedRows = writeRepository.update(programmerId, request);
        if (changedRows == 0) { // 변경된 행이 0개인 경우 (찾지 못한 경우)
            log.warn("[ProgrammerService] Update failed: Programmer not found with ID: {}. No rows changed.", programmerId);
            throw new NotFoundProgrammerException(
                    ErrorMessage.NOT_FOUND_PROGRAMMER,
                    String.format("ID '%d'를 가진 Programmer를 찾을 수 없어 업데이트를 진행할 수 없습니다.", programmerId)
            );
        }
        log.info("[ProgrammerService] Programmer with ID {} updated successfully. Changed rows: {}", programmerId, changedRows);
    }

    /**
     * 특정 프로그래머 계정을 삭제합니다.
     * 계정 삭제 시, 해당 프로그래머가 작성한 모든 답변도 함께 삭제됩니다.
     *
     * @param programmerId 삭제할 프로그래머의 ID
     * @throws com.codingmate.exception.exception.programmer.NotFoundProgrammerException 지정된 {@code programmerId}를 가진 프로그래머를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional
    public void delete(Long programmerId) {
        log.info("[ProgrammerService] delete({})", programmerId);

        log.debug("[ProgrammerService] Checking existence for programmer ID: {}", programmerId);
        boolean isExist = defaultProgrammerRepository.existsById(programmerId);
        if (isExist) {
            log.debug("[ProgrammerService] Programmer with ID {} exists. Proceeding with deletion.", programmerId);

            long deletedAnswersCount = answerWriteRepository.deleteByProgrammerId(programmerId);
            log.info("[ProgrammerService] Programmer ID {} related answers {} were deleted.", programmerId, deletedAnswersCount);

            defaultProgrammerRepository.deleteById(programmerId);
            log.info("[ProgrammerService] Programmer with ID {} deleted successfully.", programmerId);
        } else {
            log.warn("[ProgrammerService] Delete failed: Programmer not found with ID: {}. No action taken.", programmerId);
            throw new NotFoundProgrammerException(
                    ErrorMessage.NOT_FOUND_PROGRAMMER,
                    String.format("ID '%d'를 가진 Programmer를 찾을 수 없어 삭제를 진행할 수 없습니다.", programmerId)
            );
        }
    }
}