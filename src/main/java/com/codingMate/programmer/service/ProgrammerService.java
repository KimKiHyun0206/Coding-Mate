package com.codingMate.programmer.service;

import com.codingMate.answer.repository.AnswerReadRepository;
import com.codingMate.answer.repository.AnswerWriteRepository;
import com.codingMate.programmer.domain.Programmer;
import com.codingMate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingMate.programmer.dto.request.ProgrammerUpdateRequest;
import com.codingMate.programmer.dto.response.MyPageResponse;
import com.codingMate.exception.dto.ErrorMessage;
import com.codingMate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.exception.exception.programmer.ProgrammerNotCreateException;
import com.codingMate.programmer.repository.DefaultProgrammerRepository;
import com.codingMate.programmer.repository.ProgrammerReadRepository;
import com.codingMate.programmer.repository.ProgrammerWriteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerService {
    private final DefaultProgrammerRepository defaultProgrammerRepository;
    private final ProgrammerWriteRepository writeRepository;
    private final ProgrammerReadRepository readRepository;
    private final AnswerReadRepository answerReadRepository;
    private final AnswerWriteRepository answerWriteRepository;

    @Transactional
    public void create(ProgrammerCreateRequest request) {
        if (readRepository.isExistLoginId(request.loginId())) {
            throw new DuplicateProgrammerLoginIdException(
                    ErrorMessage.DUPLICATE_PROGRAMMER_EXCEPTION,
                    "요청한 Id는 이미 존재하는 Id입니다"
            );
        }

        Programmer result = writeRepository.create(request)
                .orElseThrow(() -> new ProgrammerNotCreateException(
                        ErrorMessage.PROGRAMMER_NOT_CREATED,
                        "요청에 따른 PROGRAMMER가 생성되지 않았습니다.")
                );
    }

    @Transactional(readOnly = true)
    public void checkLoginIdAvailability(String loginId) {
        if (readRepository.isExistLoginId(loginId)) {
            throw new DuplicateProgrammerLoginIdException(
                    ErrorMessage.DUPLICATE_PROGRAMMER_EXCEPTION,
                    "요청한 ID는 중복된 ID입니다. " + loginId
            );
        }
    }

    @Transactional(readOnly = true)
    public MyPageResponse getProgrammerMyPageInfo(Long programmerId) {
        var programmer = readRepository.read(programmerId)
                .orElseThrow(() -> new NotFoundProgrammerException(
                        ErrorMessage.INVALID_ID,
                        programmerId + " 는 존재하지 않는 유저 정보입니다")
                );

        return MyPageResponse.builder()
                .name(programmer.getName().getName())
                .tip(programmer.getTip())
                .numberOfAnswer(answerReadRepository.countProgrammerWroteAnswer(programmerId))
                .email(programmer.getEmail().getEmail())
                .githubId(programmer.getGithubId())
                .build();
    }

    @Transactional
    public void update(Long programmerId, ProgrammerUpdateRequest request) {
        long changedRows = writeRepository.update(programmerId, request);
        if(changedRows == 0) { // 변경된 행이 0개인 경우 (찾지 못한 경우)
            throw new NotFoundProgrammerException(
                    ErrorMessage.NOT_FOUND_PROGRAMMER_EXCEPTION,
                    String.format("ID '%d'를 가진 Programmer를 찾을 수 없어 업데이트를 진행할 수 없습니다.", programmerId)
            );
        }
    }

    @Transactional
    public void delete(Long programmerId) {
        boolean isExist = defaultProgrammerRepository.existsById(programmerId);
        if (isExist) {
            long deletedAnswersCount  = answerWriteRepository.deleteByProgrammerId(programmerId);
            log.info("Programmer ID {}와 관련된 답변 {}개가 삭제되었습니다.", programmerId, deletedAnswersCount);
            defaultProgrammerRepository.deleteById(programmerId);
        } else {
            throw new NotFoundProgrammerException(
                    ErrorMessage.NOT_FOUND_PROGRAMMER_EXCEPTION,
                    String.format("ID '%d'를 가진 Programmer를 찾을 수 없어 삭제를 진행할 수 없습니다.", programmerId)
            );
        }

    }
}