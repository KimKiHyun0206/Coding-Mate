package com.codingmate.programmer.service;

import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerFinder {
    private final DefaultProgrammerRepository programmerRepository;

    @Transactional(readOnly = true)
    public Programmer read(Long id){
        return programmerRepository.findById(id)
                .orElseThrow(() -> new NotFoundProgrammerException(
                        ErrorMessage.NOT_FOUND_PROGRAMMER_EXCEPTION,
                        "프로그래머를 찾을 수 없습니다.")
                );
    }
}
