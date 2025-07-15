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
     * @param programmerId Answer를 작성할 Programmer의 ID
     * @param request      생성할 Answer의 내용을 담은 DTO
     * @return 생성된 Answer의 ID를 포함하는 응답 DTO
     * @throws NotFoundProgrammerException 지정된 {@code programmerId}를 가진 Programmer를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional
    public AnswerCreateResponse create(Long programmerId, AnswerCreateRequest request) {
        log.debug("[AnswerService] create({}, {})", programmerId, request.toString());

        log.info("[AnswerService] Attempting to read Programmer: {}", programmerId);
        var writer = defaultProgrammerRepository.findById(programmerId)
                .orElseThrow(() -> {
                    log.warn("[AnswerService] Programmer not found with ID: {}", programmerId);
                    return new NotFoundProgrammerException(
                            ErrorMessage.NOT_FOUND_PROGRAMMER,
                            String.format("ID %d를 가진 Programmer를 찾을 수 없습니다.", programmerId)
                    );
                });
        log.debug("[AnswerService] Programmer found: {}", writer.getId());
        //log.trace("[AnswerService] Programmer details: {}", writer.toString());

        log.info("[AnswerService] Saving new Answer for programmer ID: {}", writer.getId());
        var createdResult = defaultAnswerRepository.save(Answer.toEntity(request, writer));
        log.debug("[AnswerService] Answer created: {}", createdResult.getId());

        return new AnswerCreateResponse(createdResult.getId());
    }

    /**
     * 특정 Answer의 상세 정보를 조회하고, 현재 Programmer의 해당 Answer에 대한 좋아요 여부를 반환합니다.
     *
     * @param answerId   조회할 Answer의 ID
     * @param programmerId 현재 요청을 보낸 Programmer의 ID (좋아요 여부 확인용)
     * @return Answer 상세 정보와 좋아요 여부를 포함하는 응답 DTO
     * @throws NotFoundAnswerException 지정된 {@code answerId}를 가진 Answer를 찾을 수 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public AnswerPageResponse read(Long answerId, Long programmerId) {
        log.debug("[AnswerService] read({}, {})", answerId, programmerId);

        log.info("[AnswerService] Attempting to read Answer with ID: {} for programmer ID: {}", answerId, programmerId);
        var answer = readRepository.read(answerId)
                .orElseThrow(() -> {
                    log.warn("[AnswerService] Answer not found with ID: {}", answerId);
                    return new NotFoundAnswerException(
                            ErrorMessage.NOT_FOUND_ANSWER, // ErrorMessage의 상수가 NOT_FOUND_ANSWER_EXCEPTION 이라면
                            String.format("요청한 ID %d를 가진 Answer를 찾을 수 없습니다", answerId)
                    );
                });
        log.debug("[AnswerService] Answer found: {}", answer.getId()); // 또는 answer.getTitle() 등 핵심 필드

        boolean isLiked = likeRepository.existsByProgrammerAndAnswer(answer.getProgrammer(), answer);
        log.debug("[AnswerService] Programmer {}'s like status for answer {}: {}", programmerId, answerId, isLiked);

        return AnswerPageResponse.of(answer, programmerId, isLiked);
    }

    /**
     * 모든 Answer를 {@code languageType} 및 {@code backjoonId} 기준으로 필터링하여 페이지네이션된 목록으로 반환합니다.
     *
     * @param languageType 프로그래밍 언어 타입으로 필터링 (선택 사항)
     * @param backjoonId   백준 문제 ID로 필터링 (선택 사항)
     * @param pageable     페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 등)
     * @return 필터링된 Answer 목록의 페이지 응답 DTO
     */
    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAllToListResponse(LanguageType languageType, Long backjoonId, Pageable pageable) {
        log.debug("[AnswerService] readAllToListResponse({}, {})", languageType, backjoonId);
        var result = readRepository.readAll(languageType, backjoonId, pageable);
        log.debug("[AnswerService] Answer list fetched. Total elements: {}, Page size: {}", result.getTotalElements(), result.getSize());

        return result;
    }

    /**
     * 특정 Programmer가 작성한 Answer 목록을 {@code languageType} 및 {@code backjoonId} 기준으로 필터링하여 페이지네이션된 목록으로 반환합니다.
     *
     * @param languageType 프로그래밍 언어 타입으로 필터링 (선택 사항)
     * @param backjoonId   백준 문제 ID로 필터링 (선택 사항)
     * @param programmerId Answer 작성자 Programmer의 ID
     * @param pageable     페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 등)
     * @return 필터링된 Answer 목록의 페이지 응답 DTO
     */
    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAllByProgrammerId(LanguageType languageType, Long backjoonId, Long programmerId, Pageable pageable) {
        log.debug("[AnswerService] readAllByProgrammerId({}, {})", languageType, backjoonId);
        var result = readRepository.readAllByProgrammerId(languageType, backjoonId, programmerId, pageable);
        log.debug("[AnswerService] Answer list fetched. Total elements: {}, Page size: {}", result.getTotalElements(), result.getSize());

        return result;
    }

    /**
     * 특정 Answer의 내용을 수정합니다.
     *
     * @param programmerId 수정 요청을 보낸 Programmer의 ID (권한 확인용)
     * @param answerId     수정할 Answer의 ID
     * @param request      업데이트할 내용을 담은 DTO
     * @throws NotFoundAnswerException 지정된 {@code answerId}를 가진 Answer를 찾을 수 없거나, 수정 권한이 없을 경우 발생합니다.
     */
    @Transactional
    public void update(Long programmerId, Long answerId, AnswerUpdateRequest request) {
        log.debug("[AnswerService] update({}, {})", programmerId, answerId);

        log.debug("[AnswerService] Attempting to update answer with ID: {}", answerId);
        long changedRowNumber = writeRepository.update(programmerId, answerId, request);

        if (changedRowNumber != 1) {
            log.warn("[AnswerService] Answer update failed: No answer found or authorized for answerId: {} and programmerId: {}. Changed rows: {}", answerId, programmerId, changedRowNumber);
            throw new NotFoundAnswerException(
                    ErrorMessage.NOT_FOUND_ANSWER,
                    String.format("요청한 Answer ID %d를 찾을 수 없거나, 수정 권한이 없습니다.", answerId)
            );
        }
        log.info("[AnswerService] Successfully updated answer with ID: {}. Changed rows: {}", answerId, changedRowNumber);
    }

    /**
     * 특정 Answer를 삭제합니다.
     * 삭제는 해당 Answer를 작성한 Programmer만 수행할 수 있습니다.
     *
     * @param programmerId 삭제 요청을 보낸 Programmer의 ID
     * @param answerId     삭제할 Answer의 ID
     * @throws NotFoundAnswerException 지정된 {@code answerId}를 가진 Answer를 찾을 수 없을 경우 발생합니다.
     * @throws AnswerAndProgrammerDoNotMatchException 삭제 요청을 보낸 Programmer가 해당 Answer의 작성자가 아닐 경우 발생합니다.
     */
    @Transactional
    public void delete(Long programmerId, Long answerId) {
        log.debug("[AnswerService] delete({}, {})", programmerId, answerId);

        log.info("[AnswerService] Attempting to delete answer with ID: {}", answerId);
        log.debug("[AnswerService] Retrieving answer with ID: {}", answerId);
        var answer = readRepository.read(answerId)
                .orElseThrow(() -> {
                    log.warn("[AnswerService] Answer deletion failed: Answer not found with ID: {}", answerId);
                    return new NotFoundAnswerException(
                            ErrorMessage.NOT_FOUND_ANSWER,
                            String.format("요청한 Id %d를 가진 Answer가 존재하지 않습니다.", answerId));
                });
        log.debug("[AnswerService] Answer with ID {} found. Author: {}", answer.getId(), answer.getProgrammer().getId());

        log.info("[AnswerService] Verifying if programmer {} owns answer {}.", programmerId, answerId);
        if (answer.getProgrammer().getId().equals(programmerId)) {
            log.info("[AnswerService] Programmer {} is authorized to delete answer {}. Proceeding with deletion.", programmerId, answerId);
            defaultAnswerRepository.delete(answer);
            log.info("[AnswerService] Answer with ID {} deleted successfully by programmer {}.", answerId, programmerId);
        } else {
            log.warn("[AnswerService] Answer deletion failed: Programmer {} does not own answer {}. Unauthorized attempt.", programmerId, answerId);
            throw new AnswerAndProgrammerDoNotMatchException(
                    ErrorMessage.ANSWER_AND_PROGRAMMER_DO_NOT_MATCH,
                    String.format("Programmer ID %d가 삭제 요청한 Answer ID %d는 요청자가 작성한 Answer가 아닙니다.", programmerId, answerId)
            );
        }
    }
}