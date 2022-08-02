package com.bamboo.baekjoon.domain.checks;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "check_history")
public class CheckHistory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Column(name = "prob_no", nullable = false)
    private int probNo;

    @Column(name = "prob_tier", nullable = false)
    private int probTier;

    @Column(name = "solved_at", nullable = false)
    private LocalDateTime solvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_id", nullable = false)
    private Checks check;

    @Builder
    public CheckHistory(int probNo, int probTier, LocalDateTime solvedAt) {
        this.probNo = probNo;
        this.probTier = probTier;
        this.solvedAt = solvedAt;
    }

    public void setCheck(Checks check) {
        if (this.check != null)
            this.check.getCheckHistoryList().remove(this);

        this.check = check;
        check.getCheckHistoryList().add(this);
    }

    @Override
    public String toString() {
        return "CheckHistory{" +
                "id=" + id +
                ", probNo=" + probNo +
                ", probTier=" + probTier +
                ", solvedAt=" + solvedAt +
                ", check=" + check.getId() +
                '}';
    }
}
