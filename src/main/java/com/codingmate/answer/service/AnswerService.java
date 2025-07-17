package com.codingmate.answer.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.repository.AnswerReadRepository;
import com.codingmate.answer.repository.AnswerWriteRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.codingmate.answer.dto.response.AnswerCreateResponse;
import com.codingmate.answer.dto.response.AnswerListResponse;
import com.codingmate.answer.dto.response.AnswerPageResponse;
import com.codingmate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.like.repository.LikeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Answer 엔티티에 대한 기본적인 CRUD를 구현한 서비스
 *
 * @author duskafka
 * */
@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerService {
    private final AnswerReadRepository readRepository;
    private final AnswerWriteRepository writeRepository;
    private final DefaultAnswerRepository defaultAnswerRepository;
    private final DefaultProgrammerRepository defaultProgrammerRepository;
    private final LikeRepository likeRepository;

    /**
     * 새로운 Answer를 생성합니다.
     *
     * @param username  풀이를 작성할 사용자의 {@code username}
     * @param request   생성할 Answer의 내용을 담은 DTO
     * @return 생성된 Answer의 ID를 포함하는 응답 DTO
     * @throws NotFoundProgrammerException 지정된 {@code username}를 가진 사용자를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional
    public AnswerCreateResponse create(String username, AnswerCreateRequest request) {
        log.debug("[AnswerService] create({}, {})", username, request.toString());

        var writer = defaultProgrammerRepository.findByLoginId(username)
                .orElseThrow(() -> new NotFoundProgrammerException(
                        ErrorMessage.NOT_FOUND_PROGRAMMER,
                        String.format("요청한 사용자(%s)를 찾을 수 없습니다.", username)
                ));

        var createdResult = defaultAnswerRepository.save(Answer.toEntity(request, writer));

        log.info("[AnswerService] 풀이 생성 완료: answerId={}, username={}", createdResult.getId(), username);
        return new AnswerCreateResponse(createdResult.getId());
    }

    /**
     * 특정 풀이의 상세 정보를 조회하고, 현재 사용자의 해당 풀이에 대한 좋아요 여부를 반환한다.
     *
     * @param answerId  조회할 풀이의 PK
     * @param username  현재 요청을 보낸 사용자의 {@code username}이며 좋아요 여부 확인을 위해 필요하다.
     * @return Answer 상세 정보와 좋아요 여부를 포함하는 응답 DTO
     * @throws NotFoundAnswerException 지정된 {@code answerId}를 가진 Answer를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public AnswerPageResponse read(Long answerId, String username) {
        log.debug("[AnswerService] read({})", username);
        return readRepository.read(answerId)
                .map(answer -> {
                    boolean isLiked = likeRepository.existsByProgrammerAndAnswer(answer.getProgrammer(), answer);
                    var response = AnswerPageResponse.of(answer, username, isLiked);

                    log.info("[AnswerService] 풀이 조회 성공: answerId={}, byUser={}", answerId, username);
                    return response;
                }).orElseThrow(() -> new NotFoundAnswerException(
                        ErrorMessage.NOT_FOUND_ANSWER,
                        String.format("요청한 풀이(%d)를 찾을 수 없습니다.", answerId)
                ));
    }

    /**
     * 모든 풀이를 {@code languageType} 및 {@code backjoonId} 기준으로 필터링하여 페이지네이션된 목록으로 반환합니다.
     *
     * @param languageType  프로그래밍 언어 타입으로 필터링 (선택 사항)
     * @param backjoonId    백준 문제 ID로 필터링 (선택 사항)
     * @param pageable      페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 등)
     * @return 필터링된 Answer 목록의 페이지 응답 DTO
     */
    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAllToListResponse(
            LanguageType languageType,
            Long backjoonId,
            Pageable pageable
    ) {
        log.debug("[AnswerService] readAllToListResponse({}, {})", languageType, backjoonId);
        var result = readRepository.readAll(languageType, backjoonId, pageable);
        log.info("[AnswerService] 풀이 페이지 읽기 성공: pageSize={}", result.getSize());

        return result;
    }

    /**
     * 특정 사용자가 작성한 풀이 목록을 {@code languageType} 및 {@code backjoonId} 기준으로 필터링하여 페이지네이션된 목록으로 반환합니다.
     *
     * @param languageType  프로그래밍 언어 타입으로 필터링 (선택 사항)
     * @param backjoonId    백준 문제 ID로 필터링 (선택 사항)
     * @param loginId       Answer 작성자 Programmer의 ID
     * @param pageable      페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 등)
     * @return 필터링된 Answer 목록의 페이지 응답 DTO
     */
    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAllByProgrammerId(
            LanguageType languageType,
            Long backjoonId,
            String loginId,
            Pageable pageable
    ) {
        log.debug("[AnswerService] readAllByProgrammerId({}, {})", languageType, backjoonId);
        var result = readRepository.readAllByProgrammerId(languageType, backjoonId, loginId, pageable);
        log.info("[AnswerService] 풀이 페이지 읽기 성공: pageSize={}", result.getSize());

        return result;
    }

    /**
     * 특정 풀이의 내용을 수정한다. 풀이 수정을 위해서는 작성자와 요청자가 동일한 사용자여야 합니다.
     *
     * @param username  수정 요청을 보낸 Programmer의 ID (권한 확인용)
     * @param answerId  수정할 Answer의 ID
     * @param request   업데이트할 내용을 담은 DTO
     * @throws NotFoundAnswerException 지정된 {@code answerId}를 가진 풀이를 찾을 수 없거나, 수정 권한이 없을 경우 발생합니다.
     */
    @Transactional
    public void update(String username, Long answerId, AnswerUpdateRequest request) {
        log.debug("[AnswerService] update({}, {})", username, answerId);

        if (writeRepository.update(username, answerId, request) != 1) {
            throw new NotFoundAnswerException(
                    ErrorMessage.NOT_FOUND_ANSWER,
                    String.format("요청한 풀이(%d)를 찾을 수 없거나, 수정 권한이 없습니다.", answerId)
            );
        }

        log.info("[AnswerService] 풀이 업데이트가 성공적으로 수행되었습니다: answerId={}, programmer={}", answerId, username);
    }

    /**
     * 풀이를 삭제합니다. 풀이를 삭제하기 위해서는 작성자와 요청자가 동일한 사용자여야 합니다.
     *
     * @param username  삭제 요청을 보낸 사용자의 {@code username}
     * @param answerId  삭제할 풀이의 PK
     * @throws NotFoundAnswerException                  지정된 {@code answerId}를 가진 풀이를 찾을 수 없을 경우 발생한다.
     * @throws AnswerAndProgrammerDoNotMatchException   삭제 요청을 보낸 사용자가 풀이를 작성한 작성자와 일치하지 않을 경우 발생한다.
     */
    @Transactional
    public void delete(String username, Long answerId) {
        log.debug("[AnswerService] delete({}, {})", username, answerId);

        var answer = readRepository.read(answerId)
                .orElseThrow(() -> new NotFoundAnswerException(
                        ErrorMessage.NOT_FOUND_ANSWER,
                        String.format("요청한 풀이(%d)를 찾을 수 없습니다.", answerId)
                ));

        String writerUsername = answer.getProgrammer().getLoginId();
        if (writerUsername.equals(username)) {
            defaultAnswerRepository.delete(answer);
            log.info("[AnswerService] 풀이가 삭제되었습니다: answerId={}, username={}", answerId, username);
        } else {
            throw new AnswerAndProgrammerDoNotMatchException(
                    ErrorMessage.ANSWER_AND_PROGRAMMER_DO_NOT_MATCH,
                    String.format("사용자(%s)가 삭제 요청한 풀이(%d)는 요청자가 작성한 풀이가 아닙니다.", username, answerId)
            );
        }
    }
}