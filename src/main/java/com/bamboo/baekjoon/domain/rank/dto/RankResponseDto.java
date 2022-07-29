package com.bamboo.baekjoon.domain.rank.dto;

import com.bamboo.baekjoon.domain.rank.AccumRank;
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

        public static Score of(AccumRank accumRank) {
            return Score.builder()
                    .id(accumRank.getId())
                    .scoreTotal(accumRank.getScoreTotal())
                    .scoreChallenge(accumRank.getScoreChallenge())
                    .scoreFail(accumRank.getScoreFail())
                    .userInfo(UserResponseDto.Info.of(accumRank.getUser()))
                    .build();
        }
    }
}
