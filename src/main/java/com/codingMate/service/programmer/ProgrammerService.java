package com.codingMate.service.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.request.programmer.ProgrammerUpdateRequest;
import com.codingMate.dto.response.programmer.MyPageResponse;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.repository.programmer.CustomProgrammerRepository;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
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

    @Transactional
    public ProgrammerDto create(ProgrammerCreateRequest request) {
        return customProgrammerRepository.create(request).toDto();
    }

    @Transactional(readOnly = true)
    public ProgrammerDto read(Long id) {
        return customProgrammerRepository.read(id).toDto();
    }

    @Transactional(readOnly = true)
    public MyPageResponse myPage(Long id){
        return customProgrammerRepository.read(id).toMyPateDto();
    }

    @Transactional(readOnly = true)
    public List<ProgrammerDto> readAll() {
        return customProgrammerRepository.readAll().stream().map(Programmer::toDto).toList();
    }

    @Transactional
    public Long readIdByUserName(String username) {
        return customProgrammerRepository.readIdByUserName(username);
    }

    @Transactional
    public ProgrammerDto update(Long programmerId, ProgrammerUpdateRequest request) {
        return customProgrammerRepository.update(programmerId, request).toDto();
    }

    @Transactional
    public boolean delete(Long programmerId) {
        boolean isExist = defaultProgrammerRepository.existsById(programmerId);
        if (isExist) {
            defaultProgrammerRepository.deleteById(programmerId);
        }else throw new NotFoundProgrammerException(programmerId);

        return true;
    }
}