package com.codingMate.programmer.service;

import com.codingMate.answer.repository.AnswerReadRepository;
import com.codingMate.answer.repository.AnswerWriteRepository;
import com.codingMate.programmer.domain.Programmer;
import com.codingMate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingMate.programmer.dto.request.ProgrammerUpdateRequest;
import com.codingMate.programmer.dto.response.MyPageResponse;
import com.codingMate.programmer.dto.response.ProgrammerCreateResponse;
import com.codingMate.programmer.dto.response.ProgrammerResponse;
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

import java.util.List;

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
        if (readRepository.isExistLoginId(request.loginId()))
            throw new DuplicateProgrammerLoginIdException(ErrorMessage.DUPLICATE_PROGRAMMER_EXCEPTION, "요청한 Id는 이미 존재하는 Id입니다");

        Programmer result = writeRepository.create(request).orElseThrow(() -> new ProgrammerNotCreateException(ErrorMessage.PROGRAMMER_NOT_CREATED, "요청에 따른 PROGRAMMER가 생성되지 않았습니다."));

    }

    @Transactional(readOnly = true)
    public void isExistLoginId(String loginId) {
        if (readRepository.isExistLoginId(loginId))
            throw new DuplicateProgrammerLoginIdException(ErrorMessage.DUPLICATE_PROGRAMMER_EXCEPTION, "요청한 ID는 중복된 ID입니다. " + loginId);
    }

    @Transactional(readOnly = true)
    public MyPageResponse myPage(Long id) {
        var programmer = readRepository.read(id).orElseThrow(() -> new NotFoundProgrammerException("요청한 Programmer를 조회할 수 없습니다. " + id));

        return MyPageResponse.builder()
                .name(programmer.getName().getName())
                .tip(programmer.getTip())
                .numberOfAnswer(answerReadRepository.countProgrammerWroteAnswer(id))
                .email(programmer.getEmail().getEmail())
                .githubId(programmer.getGithubId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProgrammerResponse> readAll() {
        List<ProgrammerResponse> list = readRepository.readAll().stream().map(ProgrammerResponse::from).toList();
        if (list.isEmpty()) throw new NotFoundProgrammerException("전체 Programmer를 조회할 수 없습니다.");
        return list;
    }

    @Transactional
    public void update(Long programmerId, ProgrammerUpdateRequest request) {
        long changedRow = writeRepository.update(programmerId, request);
        if (changedRow != 1)
            throw new NotFoundProgrammerException("요청한 Programmer를 조회할 수 없습니다. 따라서 Update또한 이루어지지 않았습니다" + programmerId);
    }

    @Transactional
    public void delete(Long id) {
        boolean isExist = defaultProgrammerRepository.existsById(id);
        if (isExist) {
            long delete = answerWriteRepository.deleteByProgrammerId(id);
            log.info("deleted answer {}", delete);
            defaultProgrammerRepository.deleteById(id);
        } else throw new NotFoundProgrammerException("요청한 Programmer를 조회할 수 없습니다. 따라서 Delete또한 이루어지지 않았습니다" + id);

    }
}