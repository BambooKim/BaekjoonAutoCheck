package com.bamboo.baekjoon.domain.checks;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import com.bamboo.baekjoon.domain.user.Users;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CheckHistory extends BaseTimeEntity {

    @Id @GeneratedValue
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
