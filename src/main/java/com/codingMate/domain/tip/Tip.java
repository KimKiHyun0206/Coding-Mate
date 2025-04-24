package com.codingMate.domain.tip;

import com.codingMate.dto.response.tip.TipDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tip {

    @Id
    @GeneratedValue
    @Column(name = "tip_id")
    private Long id;

    @Setter
    @Column(name = "content", length = 2000)
    private String content;

    @Setter
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