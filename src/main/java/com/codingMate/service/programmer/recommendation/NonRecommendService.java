package com.codingMate.service.programmer.recommendation;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.domain.tip.Tip;
import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.dto.response.comment.CommentDto;
import com.codingMate.dto.response.tip.TipDto;
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
public class NonRecommendService {
    private final DefaultAnswerRepository defaultAnswerRepository;
    private final DefaultProgrammerRepository defaultProgrammerRepository;
    private final DefaultCommentRepository defaultCommentRepository;
    private final DefaultTipRepository defaultTipRepository;

    @Transactional
    public AnswerDto nonRecommendAnswer(Long programmerId, Long answerId){
        Programmer programmer = findProgrammerById(programmerId);
        Answer answer = findAnswerById(answerId);

        if(programmer.getRecommendationAnswers().contains(answer)){
            programmer.getRecommendationAnswers().remove(answer);
            answer.nonRecommend();
            return answer.toDto();
        }else throw new ProgrammerNotRecommendAnswerException(programmerId, answerId);
    }

    @Transactional
    public CommentDto nonRecommendComment(Long programmerId, Long commentId){
        Comment comment = findCommentById(commentId);
        Programmer programmer = findProgrammerById(programmerId);

        if(programmer.getRecommendationComments().contains(comment)){
            programmer.getRecommendationComments().remove(comment);
            comment.nonRecommend();
            return comment.toDto();
        }else throw new ProgrammerNotRecommendCommentException(programmerId, commentId);
    }

    @Transactional
    public TipDto nonRecommendTip(Long programmerId, Long tipId){
        Tip tip = findTipById(tipId);
        Programmer programmer = findProgrammerById(programmerId);

        if(programmer.getRecommendationTips().contains(tip)){
            programmer.getRecommendationTips().remove(tip);
            tip.nonRecommend();
            return tip.toDto();
        }else throw new ProgrammerNotRecommendTipException(programmerId, tipId);
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
