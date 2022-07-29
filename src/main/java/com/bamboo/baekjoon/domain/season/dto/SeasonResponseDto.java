package com.bamboo.baekjoon.domain.season.dto;

import com.bamboo.baekjoon.domain.season.Season;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeasonResponseDto {

    private Long id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public static SeasonResponseDto of(Season season) {
        return SeasonResponseDto.builder()
                .id(season.getId())
                .name(season.getName())
                .startAt(season.getStartAt())
                .endAt(season.getEndAt())
                .build();
    }
}
