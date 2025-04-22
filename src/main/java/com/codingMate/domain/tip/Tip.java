package com.codingMate.domain.tip;

import com.codingMate.dto.tip.TipDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tip {

    @Id
    @GeneratedValue
    @Column(name = "tip_id")
    private Long id;

    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "recommendation")
    private Long recommendation = 0L;

    public Tip(String content) {
        this.content = content;
    }

    public TipDto toDto(){
        return new TipDto(id, content, recommendation);
    }

    public void recommend() {
        this.recommendation++;
    }

    public void nonRecommend() {
        this.recommendation--;
    }

    public void updateTip(String content) {
        this.content = content;
    }
}