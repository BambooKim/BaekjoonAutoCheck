package com.bamboo.baekjoon.domain.rank.service;

import com.bamboo.baekjoon.domain.checks.CheckStatus;
import com.bamboo.baekjoon.domain.checks.Checks;
import com.bamboo.baekjoon.domain.checks.repository.CheckRepository;
import com.bamboo.baekjoon.domain.rank.AccumRank;
import com.bamboo.baekjoon.domain.rank.repository.AccumRankRepository;
import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.season.repository.SeasonRepository;
import com.bamboo.baekjoon.domain.term.Term;
import com.bamboo.baekjoon.domain.term.repository.TermRepository;
import com.bamboo.baekjoon.domain.user.Users;
import lombok.RequiredArgsConstructor;
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
    private final AccumRankRepository accumRankRepository;
    private final TermRepository termRepository;
    private final SeasonRepository seasonRepository;

    @Override
    public void updateAccumRank(Long seasonId) {
        Season findSeason = seasonRepository.findById(seasonId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Season이 존재하지 않습니다.");
        });

        List<Term> findTerms = termRepository.findBySeasonIs(findSeason);
        List<Checks> findChecks = checkRepository.findByStatusIsAndTermIn(CheckStatus.COMPLETE, findTerms);

        Map<Users, AccumRank> accumRankMap = new HashMap<>();
        accumRankRepository.findBySeasonIs(findSeason).forEach(accumRank -> accumRankMap.put(accumRank.getUser(), accumRank));

        for (Checks findCheck : findChecks) {
            if (findCheck.isRankApplied())
                continue;

            Users user = findCheck.getUser();
            AccumRank accumRank = accumRankMap.get(user);

            // 벌금왕 누적 랭킹 Update
            if (!findCheck.isSuccess()) {
                accumRank.addScoreFail(1);
            }

            findCheck.getCheckHistoryList().forEach(history -> {
                // 누적 랭킹 Update
                accumRank.addScoreTotal(history.getProbTier());

                // 도전왕 랭킹 Update
                if (history.getProbTier() > user.getUserTier()) {
                    accumRank.addScoreChallenge(history.getProbTier() - user.getUserTier());
                }
            });

            findCheck.completeRankApply();
        }
    }
}
