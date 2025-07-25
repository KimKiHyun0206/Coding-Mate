package com.codingmate.programmer.service;

import com.codingmate.answer.repository.AnswerReadRepository;
import com.codingmate.answer.repository.AnswerWriteRepository;
import com.codingmate.auth.domain.Authority;
import com.codingmate.auth.service.AuthorityFinder;
import com.codingmate.email.repository.EmailVerificationTokenRepository;
import com.codingmate.email.service.EmailVerificationService;
import com.codingmate.exception.exception.email.EmailNotVerificationException;
import com.codingmate.exception.exception.programmer.ProgrammerUpdateFailedException;
import com.codingmate.programmer.domain.vo.Email;
import com.codingmate.programmer.domain.vo.Name;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.dto.request.ProgrammerUpdateRequest;
import com.codingmate.programmer.dto.response.MyPageResponse;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.programmer.dto.response.ProgrammerResponse;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.programmer.repository.ProgrammerReadRepository;
import com.codingmate.programmer.repository.ProgrammerWriteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Programmer의 CRUD를 담당하는 서비스
 *
 * @author duskafka
 */
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
    private final EmailVerificationService emailVerificationService;


    /**
     * 새로운 프로그래머 계정을 생성합니다.
     * 계정 생성 전에 로그인 ID의 중복 여부를 확인합니다.
     *
     * @param request 생성할 프로그래머의 정보를 담은 DTO
     * @throws DuplicateProgrammerLoginIdException 요청한 {@code loginId}가 이미 존재할 경우 발생합니다.
     */
    @Transactional
    public void create(ProgrammerCreateRequest request) {
        log.debug("[ProgrammerService] create({})", request.loginId());

        if (readRepository.isExistLoginId(request.loginId())) {
            throw new DuplicateProgrammerLoginIdException(
                    ErrorMessage.DUPLICATE_PROGRAMMER,
                    "요청한 Id는 이미 존재하는 Id입니다"
            );
        }

        if (!emailVerificationService.isVerified(request.email())) {
            throw new EmailNotVerificationException(
                    ErrorMessage.EMAIL_NOT_VERIFICATION,
                    String.format("요청된 이메일(%s)는 인증된 이메일이 아니기에 회원가입이 거부됩니다.", request.email())
            );
        }

        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityFinder.getUserAuthority("ROLE_USER"));
        writeRepository.create(request, authorities);

        log.info("[ProgrammerService] 사용자 계정이 성공적으로 생성되었습니다: username={}", request.loginId());
    }

    /**
     * 주어진 로그인 ID의 사용 가능 여부를 확인합니다.
     *
     * @param loginId 중복 여부를 확인할 로그인 ID
     * @throws DuplicateProgrammerLoginIdException 요청한 {@code loginId}가 이미 존재하여 사용할 수 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public void checkLoginIdAvailability(String loginId) {
        log.debug("[ProgrammerService] checkLoginIdAvailability({})", loginId);

        if (readRepository.isExistLoginId(loginId)) {
            throw new DuplicateProgrammerLoginIdException(
                    ErrorMessage.DUPLICATE_PROGRAMMER,
                    String.format("요청한 로그인 아이디(%s)는 중복된 아이디입니다.", loginId)
            );
        }

        log.info("[ProgrammerService] 로그인 아이디 {}는 사용 가능합니다.", loginId);
    }

    /**
     * 사용자의 마이페이지 정보를 조회합니다.
     *
     * @param username 조회할 프로그래머의 username
     * @return 프로그래머의 마이페이지 정보를 담은 응답 DTO
     * @throws NotFoundProgrammerException 지정된 {@code programmerId}를 가진 프로그래머를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public MyPageResponse getProgrammerMyPageInfo(String username) {
        log.debug("[ProgrammerService] getProgrammerMyPageInfo({})", username);
        return defaultProgrammerRepository.findByLoginId(username)
                .map(programmer -> {
                    long answersCount = answerReadRepository.countProgrammerWroteAnswer(username);
                    log.info("[ProgrammerService] 사용자를 찾았으며 사용자가 작성한 풀이의 수는 {} 입니다.", answersCount);
                    return MyPageResponse.of(programmer, answersCount);
                })
                .orElseThrow(() -> new NotFoundProgrammerException(
                        ErrorMessage.NOT_FOUND_PROGRAMMER,
                        String.format("사용자(%s)를 찾을 수 없습니다.", username)
                ));
    }

    @Transactional(readOnly = true)
    public ProgrammerResponse findByLoginId(String loginId) {
        log.debug("[ProgrammerService] findByLoginId({})", loginId);
        return ProgrammerResponse.of(readRepository.readByLoginId(loginId).orElseThrow(() ->
                new NotFoundProgrammerException(
                        ErrorMessage.INVALID_ID,
                        String.format("요청한 로그인 아이디(%s)는 존재하지 않습니다.", loginId)))
        );
    }

    /**
     * 사용자의 정보를 수정합니다.
     *
     * @param username 수정할 프로그래머의 username
     * @param request  수정할 내용을 담은 DTO
     * @throws NotFoundProgrammerException 지정된 {@code programmerId}를 가진 프로그래머를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional
    public void update(String username, ProgrammerUpdateRequest request) {
        log.debug("[ProgrammerService] update({}, {})", username, request);

        isValidUpdateRequest(request);

        long changedRows = writeRepository.update(username, request);
        if (changedRows == 0) { // 변경된 행이 0개인 경우 (찾지 못한 경우)
            throw new ProgrammerUpdateFailedException(
                    ErrorMessage.NOT_FOUND_PROGRAMMER,
                    "수정에 실패했습니다."
            );
        }
        log.info("[ProgrammerService] 수정이 정상적으로 수행되었습니다: username={}, 바뀐 행={}", username, changedRows);
    }

    private void isValidUpdateRequest(ProgrammerUpdateRequest request) {
        new Email(request.email());
        new Name(request.name());
    }

    /**
     * 사용자 계정을 삭제합니다.
     * 계정 삭제 시, 해당 프로그래머가 작성한 모든 답변도 함께 삭제됩니다.
     *
     * @param username 삭제할 프로그래머의 ID
     * @throws NotFoundProgrammerException 지정된 {@code programmerId}를 가진 프로그래머를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional
    public void delete(String username) {
        log.debug("[ProgrammerService] delete({})", username);

        boolean isExist = defaultProgrammerRepository.existsByLoginId(username);
        if (isExist) {
            long deletedAnswersCount = answerWriteRepository.deleteByLoginId(username);
            defaultProgrammerRepository.deleteByLoginId(username);
            log.info("[ProgrammerService] 사용자 정보가 정상적으로 삭제되었습니다: username={}, 삭제된 풀이 수={}", username, deletedAnswersCount);
        } else {
            throw new NotFoundProgrammerException(
                    ErrorMessage.NOT_FOUND_PROGRAMMER,
                    String.format("사용자(%s)를 찾을 수 없어 삭제에 실패했습니다.", username)
            );
        }
    }
}