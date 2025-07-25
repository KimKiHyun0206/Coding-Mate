package com.codingmate.testutil;

import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;

import java.util.UUID;

public class TestDataBuilder {
    public static ProgrammerCreateRequest createValidProgrammerCreateRequest() {
        return new ProgrammerCreateRequest(
                "test_" + UUID.randomUUID().toString().substring(0, 8),
                "github_" + UUID.randomUUID().toString().substring(0, 8),
                "validPassword123!",
                "테스트사용자",
                "test" + System.currentTimeMillis() + "@test.com"
        );
    }

    public static AnswerCreateRequest createValidAnswerCreateRequest() {
        return new AnswerCreateRequest(
                String.format("System.out.println(\"%s\");", UUID.randomUUID().toString().substring(0, 8)),
                String.format("제목: %s", UUID.randomUUID().toString().substring(0, 8)),
                String.format("설명: %s", UUID.randomUUID().toString().substring(0, 8)),
                LanguageType.JAVA,
                1L
        );
    }

    public static AnswerUpdateRequest createValidAnswerUpdateRequest() {
        return new AnswerUpdateRequest(
                String.format("System.out.println(\"%s\");", UUID.randomUUID().toString().substring(0, 8)),
                String.format("제목: %s", UUID.randomUUID().toString().substring(0, 8)),
                String.format("설명: %s", UUID.randomUUID().toString().substring(0, 8)),
                LanguageType.JAVA,
                2L
        );
    }
}