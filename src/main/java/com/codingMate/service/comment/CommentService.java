package com.codingMate.service.comment;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.comment.CommentCreateDto;
import com.codingMate.dto.request.comment.CommentUpdateDto;
import com.codingMate.dto.response.comment.CommentDto;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.comment.NotFoundCommentException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.repository.answer.DefaultAnswerRepository;
import com.codingMate.repository.comment.DefaultCommentRepository;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.domain.comment.QComment.comment;

@Slf4j
@Service
public class CommentService {
    private final DefaultCommentRepository commentRepository;
    private final DefaultProgrammerRepository programmerRepository;
    private final DefaultAnswerRepository answerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public CommentService(EntityManager em, DefaultCommentRepository commentRepository, DefaultProgrammerRepository programmerRepository, DefaultAnswerRepository answerRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.commentRepository = commentRepository;
        this.programmerRepository = programmerRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public CommentDto create(Long programmerId, CommentCreateDto dto) {
        Programmer programmer = programmerRepository
                .findById(programmerId)
                .orElseThrow(() -> new NotFoundProgrammerException(programmerId));

        Answer answer = answerRepository
                .findById(dto.getAnswerId())
                .orElseThrow(() -> new NotFoundAnswerException(dto.getAnswerId()));

        Comment entity = dto.toEntity();
        entity.setAnswer(answer);
        entity.setProgrammer(programmer);

        Comment saved = commentRepository.save(entity);

        programmer.getComments().add(entity);
        answer.getComments().add(entity);
        return saved.toDto();
    }

    @Transactional(readOnly = true)
    public CommentDto read(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(commentId))
                .toDto();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> readByAnswerId(Long answerId){
        return commentRepository
                .readCommentsByAnswerId(answerId)
                .stream()
                .map(Comment::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> readByProgrammerId(Long programmerId){
        return commentRepository
                .readCommentsByProgrammerId(programmerId)
                .stream()
                .map(Comment::toDto)
                .toList();
    }

    @Transactional
    public CommentDto update(Long programmerId, CommentUpdateDto dto) {
        long executed = queryFactory.update(comment)
                .where(comment.id.eq(dto.getId()))
                .where(comment.programmer.id.eq(programmerId))
                .set(comment.content, dto.getComment() == null ? null : dto.getComment())
                .execute();
        if(executed == 0) throw new NotFoundCommentException(dto.getId());

        return read(dto.getId());
    }

    @Transactional
    public boolean delete(Long programmerId, Long commentId) {
        long executed = queryFactory.delete(comment)
                .where(comment.id.eq(commentId))
                .where(comment.programmer.id.eq(programmerId))
                .execute();
        if(executed == 0) throw new NotFoundCommentException(commentId);
        return true;
    }
}
