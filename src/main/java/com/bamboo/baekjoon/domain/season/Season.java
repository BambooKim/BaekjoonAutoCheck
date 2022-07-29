package com.bamboo.baekjoon.domain.season;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
