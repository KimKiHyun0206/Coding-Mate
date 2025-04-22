package com.codingMate.repository.comment;

import com.codingMate.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultCommentRepository extends JpaRepository<Comment, Long> {
}