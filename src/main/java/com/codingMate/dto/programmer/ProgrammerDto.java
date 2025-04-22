package com.codingMate.dto.programmer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.domain.tip.Tip;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
public class ProgrammerDto {
    private Long id;
    private String loginId;
    private String githubLink;
    private String password;
    private String name;
    private String email;
    private Tip tip;
    private List<Answer> answers;
    private List<Comment> comments;
    private List<Tip> recommendationTips;
    private List<Answer> recommendationAnswers;
    private List<Comment> recommendationComments;

    public ProgrammerDto(Long id, String loginId, String githubLink, String password, Name name, Email email, Tip tip, List<Answer> answers, List<Comment> comments, List<Tip> recommendationTips, List<Answer> recommendationAnswers, List<Comment> recommendationComments) {
        this.id = id;
        this.loginId = loginId;
        this.githubLink = githubLink;
        this.password = password;
        this.name = name.getName();
        this.email = email.getEmail();
        this.tip = tip;
        this.answers = answers;
        this.comments = comments;
        this.recommendationTips = recommendationTips;
        this.recommendationAnswers = recommendationAnswers;
        this.recommendationComments = recommendationComments;
    }
}