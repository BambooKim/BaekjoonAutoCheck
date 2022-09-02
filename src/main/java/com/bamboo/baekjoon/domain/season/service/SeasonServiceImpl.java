package com.bamboo.baekjoon.domain.season.service;

import com.bamboo.baekjoon.domain.rank.Rank;
import com.bamboo.baekjoon.domain.rank.repository.RankRepository;
import com.bamboo.baekjoon.domain.season.Season;
import com.bamboo.baekjoon.domain.season.dto.SeasonRequestDto;
import com.bamboo.baekjoon.domain.season.dto.SeasonResponseDto;
import com.bamboo.baekjoon.domain.season.repository.SeasonQueryRepository;
import com.bamboo.baekjoon.domain.season.repository.SeasonRepository;
import com.bamboo.baekjoon.domain.user.User;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final SeasonQueryRepository seasonQueryRepository;

    @Override
    public SeasonResponseDto createSeason(SeasonRequestDto requestDto) {
        validateDateTime(requestDto);

        Season season = Season.builder()
                .name(requestDto.getName())
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt())
                .build();
        seasonRepository.save(season);

        return SeasonResponseDto.of(season);
    }

    @Override
    public Long addSeasonUser(Long seasonId, List<Long> userIdList) {
        Season findSeason = seasonRepository.findById(seasonId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Season을 찾을 수 없습니다.");
        });

        List<User> findUserList = userRepository.findAllByIdIn(userIdList);

        Long rankCount = 0L;
        for (User user : findUserList) {
            Rank rank = Rank.builder()
                    .scoreTotal(0)
                    .scoreChallenge(0)
                    .scoreFail(0)
                    .user(user)
                    .season(findSeason)
                    .build();

            try {
                rankRepository.save(rank);
            } catch (DataIntegrityViolationException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 추가된 사용자입니다.");
            }

            rankCount++;
        }

        return rankCount;
    }

    @Override
    public List<SeasonResponseDto> findSeason(Long userId) {
        return seasonQueryRepository.findSeasonByUserId(userId)
                .stream()
                .map(SeasonResponseDto::of)
                .collect(Collectors.toList());
    }

    private void validateDateTime(SeasonRequestDto requestDto) {
        if (requestDto.getStartAt().isAfter(requestDto.getEndAt()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜와 종료 날짜가 유효하지 않습니다.");
    }
}
