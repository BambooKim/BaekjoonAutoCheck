package com.bamboo.baekjoon.domain.rank.service;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.repository.CheckRepository;
import com.bamboo.baekjoon.domain.rank.Rank;
import com.bamboo.baekjoon.domain.rank.dto.RankResponseDto;
import com.bamboo.baekjoon.domain.rank.repository.RankRepository;
import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.season.repository.SeasonRepository;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.term.repository.TermRepository;
import com.bamboo.baekjoon.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

    private final CheckRepository checkRepository;
    private final RankRepository rankRepository;
    private final TermRepository termRepository;
    private final SeasonRepository seasonRepository;

    @Override
    public void updateAccumRank(Long seasonId) {
        Season findSeason = seasonRepository.findById(seasonId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Season이 존재하지 않습니다.");
        });

        List<Term> findTerms = termRepository.findBySeasonIs(findSeason);
        List<Checks> findChecks = checkRepository.findByStatusIsAndTermIn(CheckStatus.COMPLETE, findTerms);

        Map<User, Rank> accumRankMap = new HashMap<>();
        rankRepository.findBySeasonIs(findSeason).forEach(accumRank -> accumRankMap.put(accumRank.getUser(), accumRank));

        for (Checks findCheck : findChecks) {
            if (findCheck.isRankApplied())
                continue;

            User user = findCheck.getUser();
            Rank rank = accumRankMap.get(user);

            // 벌금왕 누적 랭킹 Update
            if (!findCheck.isSuccess()) {
                rank.addScoreFail(1);
            }

            findCheck.getCheckHistoryList().forEach(history -> {
                // 누적 랭킹 Update
                rank.addScoreTotal(history.getProbTier());

                // 도전왕 랭킹 Update
                if (history.getProbTier() > user.getUserTier()) {
                    rank.addScoreChallenge(history.getProbTier() - user.getUserTier());
                }
            });

            findCheck.completeRankApply();
        }
    }

    @Override
    public Page<RankResponseDto.Score> getAccumRankByScore(Long seasonId, Pageable pageable) {
        Season findSeason = seasonRepository.findById(seasonId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Season이 존재하지 않습니다.");
        });

        return rankRepository.findPagesBySeasonIs(findSeason, pageable).map(RankResponseDto.Score::of);
    }
}
