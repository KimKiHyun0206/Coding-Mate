package com.codingMate.domain.tip;

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

    public void addRecommendation() {
        this.recommendation++;
    }

    public void removeRecommendation() {
        this.recommendation--;
    }

    public void updateTip(String content) {
        this.content = content;
    }
}