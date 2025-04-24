package com.codingMate.service.tip;

import com.codingMate.domain.tip.Tip;
import com.codingMate.dto.response.tip.TipDto;
import com.codingMate.exception.exception.tip.NotFoundTipException;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import com.codingMate.repository.tip.DefaultTipRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipService {
    private final DefaultProgrammerRepository programmerRepository;
    private final DefaultTipRepository tipRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public TipService(EntityManager em, DefaultProgrammerRepository programmerRepository, DefaultTipRepository tipRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.programmerRepository = programmerRepository;
        this.tipRepository = tipRepository;
    }

    @Transactional(readOnly = true)
    public TipDto read(Long id) {
        return tipRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundTipException(id)).toDto();
    }

    @Transactional
    public TipDto recommend(Long id) {
        Tip tip = tipRepository.findById(id)
                .orElseThrow(() -> new NotFoundTipException(id));
        tip.recommend();
        return tip.toDto();
    }

    @Transactional
    public TipDto nonRecommend(Long id) {
        Tip tip = tipRepository.findById(id)
                .orElseThrow(() -> new NotFoundTipException(id));
        tip.nonRecommend();
        return tip.toDto();
    }

    @Transactional
    public TipDto resetTip(Long id) {
        Tip tip = tipRepository.findById(id).orElseThrow(() -> new NotFoundTipException(id));
        tip.setContent("아무 내용이 없습니다. 나만의 팁이 있다면 공유해주세요");
        tip.setRecommendation(0L);
        return tip.toDto();
    }
}