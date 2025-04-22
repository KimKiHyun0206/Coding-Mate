package com.codingMate.service.programmer.recommendation;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.domain.tip.Tip;
import com.codingMate.dto.answer.AnswerDto;
import com.codingMate.dto.comment.CommentDto;
import com.codingMate.dto.tip.TipDto;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.comment.NotFoundCommentException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.exception.exception.recommend.ProgrammerNotRecommendAnswerException;
import com.codingMate.exception.exception.recommend.ProgrammerNotRecommendCommentException;
import com.codingMate.exception.exception.recommend.ProgrammerNotRecommendTipException;
import com.codingMate.exception.exception.tip.NotFoundTipException;
import com.codingMate.repository.answer.DefaultAnswerRepository;
import com.codingMate.repository.comment.DefaultCommentRepository;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import com.codingMate.repository.tip.DefaultTipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final DefaultAnswerRepository defaultAnswerRepository;
    private final DefaultProgrammerRepository defaultProgrammerRepository;
    private final DefaultCommentRepository defaultCommentRepository;
    private final DefaultTipRepository defaultTipRepository;

    /**
     * @param answerId 추천할 답의 id
     * @param programmerId 추천자의 id
     * */
    @Transactional
    public AnswerDto recommendAnswer(Long answerId, Long programmerId) {
        Answer answer = findAnswerById(answerId);
        Programmer programmer = findProgrammerById(programmerId);

        if (!programmer.getRecommendationAnswers().contains(answer)) {
            programmer.getRecommendationAnswers().add(answer);
            answer.recommend();
            return answer.toDto();
        }else throw new ProgrammerNotRecommendAnswerException(programmerId, answerId);
    }

    /**
     * @param commentId 추천할 댓글의 id
     * @param programmerId 추천자의 id
     * */
    @Transactional
    public CommentDto recommendComment(Long commentId, Long programmerId) {
        Comment comment = findCommentById(commentId);
        Programmer programmer = findProgrammerById(programmerId);

        if (!programmer.getRecommendationComments().contains(comment)) {
            programmer.getRecommendationComments().add(comment);
            comment.recommend();
            return comment.toDto();
        }else throw new ProgrammerNotRecommendCommentException(programmerId, commentId);
    }

    /**
     * @param tipId 추천할 팁의 id
     * @param programmerId 추천자의 id
     * */
    @Transactional
    public TipDto recommendTip(Long tipId, Long programmerId) {
        Tip tip = findTipById(tipId);
        Programmer programmer = findProgrammerById(programmerId);

        if (!programmer.getRecommendationTips().contains(tip)) {
            programmer.getRecommendationTips().add(tip);
            tip.recommend();
            return tip.toDto();
        } else throw new ProgrammerNotRecommendTipException(programmerId, tipId);
    }


    @Transactional(readOnly = true)
    public Programmer findProgrammerById(Long programmerId) {
        return defaultProgrammerRepository.findById(programmerId).orElseThrow(() ->
                new NotFoundProgrammerException(programmerId)
        );
    }

    @Transactional(readOnly = true)
    public Answer findAnswerById(Long answerId) {
        return defaultAnswerRepository.findById(answerId).orElseThrow(() ->
                new NotFoundAnswerException(answerId)
        );
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        return defaultCommentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundCommentException(commentId)
        );
    }

    @Transactional(readOnly = true)
    public Tip findTipById(Long tipId) {
        return defaultTipRepository.findById(tipId).orElseThrow(() ->
                new NotFoundTipException(tipId)
        );
    }
}