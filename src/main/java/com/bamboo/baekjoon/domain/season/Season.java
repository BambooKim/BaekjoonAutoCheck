package com.bamboo.baekjoon.domain.season;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Season {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_id")
    private Long id;

    private String name;

    @Column
    private LocalDateTime endAt;
    @Column
    private LocalDateTime startAt;
}
