package com.codingMate.repository.comment;

import com.codingMate.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultCommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> readCommentsByAnswerId(Long answerId);
    List<Comment> readCommentsByProgrammerId(Long programmerId);
}