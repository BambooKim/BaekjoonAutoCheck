package com.bamboo.baekjoon.domain.term;

import com.bamboo.baekjoon.domain.BaseTimeEntity;
import com.bamboo.baekjoon.domain.term.dto.TermRequestDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Term extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "term_id")
    private Long id;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    public void changeTerm(TermRequestDto requestDto) {
        this.startAt = requestDto.getStartAt();
        this.endAt = requestDto.getEndAt();
    }
}
