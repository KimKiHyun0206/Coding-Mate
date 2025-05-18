package com.codingMate.service.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.request.programmer.ProgrammerUpdateRequest;
import com.codingMate.dto.response.programmer.MyPageResponse;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.exception.exception.programmer.ProgrammerNotCreateException;
import com.codingMate.repository.answer.CustomAnswerRepository;
import com.codingMate.repository.programmer.CustomProgrammerRepository;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgrammerService {
    private final DefaultProgrammerRepository defaultProgrammerRepository;
    private final CustomProgrammerRepository customProgrammerRepository;
    private final CustomAnswerRepository customAnswerRepository;

    @Transactional
    public ProgrammerDto create(ProgrammerCreateRequest request) {
        if(customProgrammerRepository.isExistLoginId(request.getLoginId())) throw new DuplicateProgrammerLoginIdException();

        ProgrammerDto dto = customProgrammerRepository.create(request).toDto();
        if (dto == null) throw new ProgrammerNotCreateException("Programmer가 생성되지 않았습니다. " + request);
        dto.setLoginId(null);
        dto.setPassword(null);
        return dto;
    }

    @Transactional(readOnly = true)
    public boolean isExistLoginId(String loginId) {
        boolean isExistLoginId = customProgrammerRepository.isExistLoginId(loginId);
        if (isExistLoginId) throw new DuplicateRequestException("요청한 ID는 중복된 ID입니다. " + loginId);
        return false;
    }

    @Transactional(readOnly = true)
    public ProgrammerDto read(Long id) {
        ProgrammerDto dto = customProgrammerRepository.read(id).toDto();
        if (dto == null) throw new NotFoundProgrammerException("요청한 Programmer를 조회할 수 없습니다. " + id);
        dto.setLoginId(null);
        dto.setPassword(null);
        return dto;
    }

    @Transactional(readOnly = true)
    public MyPageResponse myPage(Long id) {
        MyPageResponse myPateDto = customProgrammerRepository.read(id).toMyPateDto();
        if (myPateDto == null) throw new NotFoundProgrammerException("요청한 Programmer를 조회할 수 없습니다. " + id);
        return myPateDto;
    }

    @Transactional(readOnly = true)
    public List<ProgrammerDto> readAll() {
        List<ProgrammerDto> list = customProgrammerRepository.readAll().stream().map(Programmer::toDto).toList();
        if (list.isEmpty()) throw new NotFoundProgrammerException("전체 Programmer를 조회할 수 없습니다.");
        return list;
    }

    @Transactional
    public Long readIdByLoginId(Long id) {
        Long result = customProgrammerRepository.read(id).getId();
        if (result == null) throw new NotFoundProgrammerException("요청한 loginId로 Programmer를 조회할 수 없습니다." + id);
        return result;
    }

    @Transactional
    public ProgrammerDto update(Long programmerId, ProgrammerUpdateRequest request) {
        ProgrammerDto dto = customProgrammerRepository.update(programmerId, request).toDto();
        if (dto == null)
            throw new NotFoundProgrammerException("요청한 Programmer를 조회할 수 없습니다. 따라서 Update또한 이루어지지 않았습니다" + programmerId);
        dto.setLoginId(null);
        dto.setPassword(null);
        return dto;
    }

    @Transactional
    public boolean delete(Long id) {
        boolean isExist = defaultProgrammerRepository.existsById(id);
        if (isExist) {
            long delete = customAnswerRepository.deleteByProgrammerId(id);
            log.info("deleted answer {}",delete);
            defaultProgrammerRepository.deleteById(id);
        } else throw new NotFoundProgrammerException("요청한 Programmer를 조회할 수 없습니다. 따라서 Delete또한 이루어지지 않았습니다" + id);

        return true;
    }
}