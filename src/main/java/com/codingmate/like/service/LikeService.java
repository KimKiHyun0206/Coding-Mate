package com.codingmate.like.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.service.AnswerFinder;
import com.codingmate.answer.service.AnswerService;

import com.codingmate.programmer.domain.Programmer;
import com.codingmate.like.domain.Like;
import com.codingmate.like.dto.response.LikeResponse;
import com.codingmate.like.repository.LikeRepository;
import com.codingmate.programmer.service.ProgrammerFinder;
import com.codingmate.programmer.service.ProgrammerService;
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

    @Transactional
    public LikeResponse toggleLike(Long programmerId, Long answerId) {
        // 1. 좋아요를 누르려는 Programmer 찾기
        var programmer = programmerFinder.read(programmerId);

        // 2. 좋아요 대상 Answer 찾기
        var answer = answerFinder.read(answerId);
        log.info(programmer.toString());
        log.info(answer.toString());

        // 3. 이미 좋아요를 눌렀는지 확인
        handleLikeStatus(programmer, answer);

        return LikeResponse.of(answer.getLikeCount());
    }

    private void handleLikeStatus(Programmer programmer, Answer answer) {
        likeRepository.findByProgrammerAndAnswer(programmer, answer).ifPresentOrElse(
                // 이미 좋아요를 눌렀다면, 좋아요 취소 (삭제)
                like -> {
                    deleteLike(answer, like);
                    log.info("Programmer {}가 Answer {}에 대한 좋아요를 취소했습니다.", programmer.getId(), answer.getId());
                },
                // 좋아요를 누르지 않았다면, 좋아요 추가 (생성)
                () -> {
                    createLike(programmer, answer);
                    log.info("Programmer {}가 Answer {}에 좋아요를 눌렀습니다.", programmer.getId(), answer.getId());
                }
        );
    }

    private void deleteLike(Answer answer, Like like) {
        answer.downVote(like); // Answer 엔티티에 좋아요 수 감소 로직 추가
    }

    private void createLike(Programmer programmer, Answer answer) {
        var like = likeRepository.save(Like.toEntity(programmer, answer));
        answer.upVote(like);
    }
}