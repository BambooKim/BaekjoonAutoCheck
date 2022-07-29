package com.bamboo.baekjoon.domain.term;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Term extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "term_id")
    private Long id;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "term")
    private List<Checks> checks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    public void changeTerm(TermRequestDto requestDto) {
        this.startAt = requestDto.getStartAt();
        this.endAt = requestDto.getEndAt();
    }

    @Builder
    public Term(LocalDateTime startAt, LocalDateTime endAt, Season season) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.season = season;
    }

    @Override
    public String toString() {
        return "Term{" +
                "id=" + id +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", checks=" + checks.size() +
                '}';
    }
}
