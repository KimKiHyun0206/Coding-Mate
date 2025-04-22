package com.codingMate.dto.programmer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.comment.Comment;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.domain.tip.Tip;
import com.codingMate.dto.answer.AnswerDto;
import com.codingMate.dto.comment.CommentDto;
import com.codingMate.dto.tip.TipDto;
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
    private TipDto tip;
    private List<AnswerDto> answers;
    private List<CommentDto> comments;
    private List<TipDto> recommendationTips;
    private List<AnswerDto> recommendationAnswers;
    private List<CommentDto> recommendationComments;

    public ProgrammerDto(Long id, String loginId, String githubLink, String password, String name, String email, TipDto tip, List<AnswerDto> answers, List<CommentDto> comments, List<TipDto> recommendationTips, List<AnswerDto> recommendationAnswers, List<CommentDto> recommendationComments) {
        this.id = id;
        this.loginId = loginId;
        this.githubLink = githubLink;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tip = tip;
        this.answers = answers;
        this.comments = comments;
        this.recommendationTips = recommendationTips;
        this.recommendationAnswers = recommendationAnswers;
        this.recommendationComments = recommendationComments;
    }
}