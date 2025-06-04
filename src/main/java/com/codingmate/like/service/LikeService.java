package com.codingmate.like.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.service.AnswerFinder;

import com.codingmate.programmer.domain.Programmer;
import com.codingmate.like.domain.Like;
import com.codingmate.like.dto.response.LikeResponse;
import com.codingmate.like.repository.LikeRepository;
import com.codingmate.programmer.service.ProgrammerFinder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeService {
    private final LikeRepository likeRepository;
    private final AnswerFinder answerFinder;
    private final ProgrammerFinder programmerFinder;

    /**
     * 특정 프로그래머가 특정 답변에 대해 '좋아요' 상태를 토글합니다.
     * 이미 좋아요를 눌렀다면 좋아요를 취소하고, 누르지 않았다면 좋아요를 추가합니다.
     * 트랜잭션 내에서 프로그래머와 답변을 조회하고, 좋아요 상태를 처리하며, 답변의 좋아요 수를 업데이트합니다.
     *
     * @param programmerId 좋아요를 누르거나 취소할 프로그래머의 ID
     * @param answerId 좋아요 대상이 되는 답변의 ID
     * @return 현재 답변의 총 좋아요 수를 포함하는 {@link LikeResponse} DTO
     */
    @Transactional
    public LikeResponse toggleLike(Long programmerId, Long answerId) {
        log.debug("[LikeService] toggleLike({}, {})", programmerId, answerId);
        log.debug("[LikeService] Starting toggleLike transaction.");

        var programmer = programmerFinder.read(programmerId);
        log.debug("[LikeService] Found Programmer: {}", programmer.getId());

        var answer = answerFinder.read(answerId);
        log.debug("[LikeService] Found Answer: {}", answer.getId());


        handleLikeStatus(programmer, answer);
        log.info("[LikeService] Like status toggled successfully. Current like count for Answer {}: {}", answer.getId(), answer.getLikeCount());

        return LikeResponse.of(answer.getLikeCount());
    }

    /**
     * 주어진 프로그래머와 답변에 대한 좋아요 상태를 확인하고 처리합니다.
     * 해당 프로그래머가 이미 이 답변에 좋아요를 눌렀는지 확인하여,
     * 좋아요가 존재하면 삭제하고, 존재하지 않으면 새로 생성합니다.
     *
     * @param programmer 좋아요를 누른 프로그래머 엔티티
     * @param answer 좋아요 대상이 된 답변 엔티티
     */
    private void handleLikeStatus(Programmer programmer, Answer answer) {
        log.debug("[LikeService] handleLikeStatus({}, {})", programmer.getId(), answer.getId());
        likeRepository.findByProgrammerAndAnswer(programmer, answer).ifPresentOrElse(
                // 이미 좋아요를 눌렀다면, 좋아요 취소 (삭제)
                like -> {
                    log.debug("[LikeService] Existing like found. Deleting like (ID: {}) for Programmer {} on Answer {}.", like.getId(), programmer.getId(), answer.getId());
                    deleteLike(answer, like);
                    log.info("[LikeService] Programmer {} canceled like on Answer {}. Current like count: {}", programmer.getId(), answer.getId(), answer.getLikeCount());
                },
                // 좋아요를 누르지 않았다면, 좋아요 추가 (생성)
                () -> {
                    log.debug("[LikeService] No existing like found. Creating new like for Programmer {} on Answer {}.", programmer.getId(), answer.getId());
                    createLike(programmer, answer);
                    log.info("[LikeService] Programmer {} liked Answer {}. Current like count: {}", programmer.getId(), answer.getId(), answer.getLikeCount());
                }
        );
    }

    /**
     * 좋아요를 취소(삭제)하는 내부 메서드입니다.
     * {@code Answer} 엔티티의 좋아요 수를 감소시키고, 실제 {@code Like} 엔티티를 데이터베이스에서 삭제합니다.
     *
     * @param answer 좋아요 취소 대상이 되는 답변 엔티티
     * @param like 삭제할 좋아요 엔티티
     */
    private void deleteLike(Answer answer, Like like) {
        log.debug("[LikeService] deleteLike({}, {}).", answer.getId(), like.getId());
        answer.downVote(like);
        log.debug("[LikeService] Downvoted Answer {} and deleted Like {}. Answer's new like count: {}", answer.getId(), like.getId(), answer.getLikeCount());
    }

    /**
     * 좋아요를 추가(생성)하는 내부 메서드입니다.
     * 새로운 {@code Like} 엔티티를 데이터베이스에 저장하고, {@code Answer} 엔티티의 좋아요 수를 증가시킵니다.
     *
     * @param programmer 좋아요를 누른 프로그래머 엔티티
     * @param answer 좋아요 대상이 되는 답변 엔티티
     */
    private void createLike(Programmer programmer, Answer answer) {
        log.debug("[LikeService] createLike({}, {})", programmer.getId(), answer.getId());
        var like = likeRepository.save(Like.toEntity(programmer, answer));
        answer.upVote(likeRepository.save(Like.toEntity(programmer, answer)));
        log.debug("[LikeService] Created new Like {} for Programmer {} on Answer {}. Answer's new like count: {}", like.getId(), programmer.getId(), answer.getId(), answer.getLikeCount());
    }
}