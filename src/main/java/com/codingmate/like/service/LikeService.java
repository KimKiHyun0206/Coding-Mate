package com.codingmate.like.service;

import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;

import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.like.domain.Like;
import com.codingmate.like.dto.response.LikeResponse;
import com.codingmate.like.repository.LikeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@ToString
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeService {
    private final LikeRepository likeRepository;
    private final DefaultProgrammerRepository defaultProgrammerRepository;
    private final DefaultAnswerRepository defaultAnswerRepository;

    @Transactional
    public LikeResponse toggleLike(Long programmerId, Long answerId) {
        // 1. 좋아요를 누르려는 Programmer 찾기
        var programmer = defaultProgrammerRepository.findById(programmerId)
                .orElseThrow(() -> new NotFoundProgrammerException(
                        ErrorMessage.NOT_FOUND_PROGRAMMER_EXCEPTION,
                        "프로그래머를 찾을 수 없습니다.")
                );

        // 2. 좋아요 대상 Answer 찾기
        var answer = defaultAnswerRepository.findById(answerId)
                .orElseThrow(() -> new NotFoundAnswerException(
                        ErrorMessage.NOT_FOUND_ANSWER_EXCEPTION,
                        "답변을 찾을 수 없습니다.")
                );
        log.info(programmer.toString());
        log.info(answer.toString());

        // 3. 이미 좋아요를 눌렀는지 확인
        likeRepository.findByProgrammerAndAnswer(programmer, answer).ifPresentOrElse(
                // 이미 좋아요를 눌렀다면, 좋아요 취소 (삭제)
                like -> {
                    answer.downVote(like); // Answer 엔티티에 좋아요 수 감소 로직 추가
                    log.info("Programmer {}가 Answer {}에 대한 좋아요를 취소했습니다.", programmerId, answerId);
                },
                // 좋아요를 누르지 않았다면, 좋아요 추가 (생성)
                () -> {
                    var newLike = Like.toEntity(programmer, answer);
                    likeRepository.save(newLike);
                    answer.upVote(newLike); // Answer 엔티티에 좋아요 수 증가 로직 추가
                    log.info("Programmer {}가 Answer {}에 좋아요를 눌렀습니다.", programmerId, answerId);
                }
        );

        return LikeResponse.of(answer.getVoteCount());
    }
}