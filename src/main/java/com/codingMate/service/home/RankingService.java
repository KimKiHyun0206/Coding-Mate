package com.codingMate.service.home;

import com.codingMate.dto.response.programmer.ProgrammerRankingDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.codingMate.domain.programmer.QProgrammer.programmer;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private LocalDate lastUpdateDate = LocalDate.now();
    private List<ProgrammerRankingDto> rankings;

    @PostConstruct
    public void init() {
        this.rankings = getRankFromDB();
    }

    public List<ProgrammerRankingDto> getRank() {
        LocalDate today = LocalDate.now();
        if(lastUpdateDate.isBefore(today)){
            lastUpdateDate = today;
            this.rankings = getRankFromDB();
        }
        return rankings;
    }

    private List<ProgrammerRankingDto> getRankFromDB() {
        return queryFactory.selectFrom(programmer)
                .orderBy(programmer.numberOfAnswer.asc())
                .limit(10)
                .fetch()
                .stream().map(p -> {
                    return new ProgrammerRankingDto(p.getName().getName(), p.getNumberOfAnswer());
                }).toList();
    }
}