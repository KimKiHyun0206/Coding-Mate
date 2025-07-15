package com.codingmate.programmer.service;

import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 단순히 Programmer를 조회하기 위한 Finder 서비스
 *
 * <li>만약 Programmer 내에 다른 연관 엔티티를 사용해야 한다면 이 클래스를 사용하면 안 된다</li>
 *
 * @author duskafka
 * */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerFinder {
    private final DefaultProgrammerRepository programmerRepository;

    @Transactional(readOnly = true)
    public Programmer read(Long id){
        return programmerRepository.findById(id)
                .orElseThrow(() -> new NotFoundProgrammerException(
                        ErrorMessage.NOT_FOUND_PROGRAMMER,
                        "프로그래머를 찾을 수 없습니다.")
                );
    }
}
