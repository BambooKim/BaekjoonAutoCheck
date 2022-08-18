package com.bamboo.baekjoon.domain.rank.dto;

import com.bamboo.baekjoon.domain.rank.Rank;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class RankResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Score {

        private Long id;
        private int scoreTotal;
        private int scoreChallenge;
        private int scoreFail;
        private UserResponseDto.Info userInfo;

        public static Score of(Rank rank) {
            return Score.builder()
                    .id(rank.getId())
                    .scoreTotal(rank.getScoreTotal())
                    .scoreChallenge(rank.getScoreChallenge())
                    .scoreFail(rank.getScoreFail())
                    .userInfo(UserResponseDto.Info.of(rank.getUser()))
                    .build();
        }
    }
}
