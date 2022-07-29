package com.bamboo.baekjoon.domain.season;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Season {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Column
    private LocalDateTime endAt;
    @Column
    private LocalDateTime startAt;
}
