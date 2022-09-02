package com.bamboo.baekjoon.domain.user.service;

import com.bamboo.baekjoon.domain.rank.repository.RankRepository;
import com.bamboo.baekjoon.domain.season.repository.SeasonRepository;
import com.bamboo.baekjoon.domain.user.Role;
import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.UserTierHistory;
import com.bamboo.baekjoon.domain.user.User;
import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.repository.TierHistoryRepository;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
import com.bamboo.baekjoon.global.config.security.JwtFilter;
import com.bamboo.baekjoon.global.config.security.Token;
import com.bamboo.baekjoon.global.config.security.TokenService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TierHistoryRepository tierHistoryRepository;
    private final RankRepository rankRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;

    private final SeasonRepository seasonRepository;

    @Override
    public UserResponseDto.Creation createUser(UserRequestDto.Creation createUserData) {

        validateDuplicateUser(createUserData.getBojId(), createUserData.getUsername());

        JsonElement element = getUserInformationByJsonElement(createUserData.getBojId());

        if (!isBojUserExists(element))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 백준 아이디를 입력해주세요.");

        int userTier = retrieveUserTier(element);

        User user = User.builder()
                .korName(createUserData.getKorName())
                .userTier(userTier)
                .enterYear(createUserData.getEnterYear())
                .bojId(createUserData.getBojId())
                .username(createUserData.getUsername())
                .password(passwordEncoder.encode(createUserData.getPassword()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        UserTierHistory tierHistory = UserTierHistory.builder()
                .beforeTier(0)
                .afterTier(userTier)
                .user(user)
                .build();
        tierHistoryRepository.save(tierHistory);

        return UserResponseDto.Creation.of(user);
    }

    @Override
    public List<UserResponseDto.Tier> updateUserTier(List<Long> userIdList) {
        List<User> users;

        if (userIdList.isEmpty())
            users = userRepository.findAllByStatusIs(UserStatus.ACTIVE);
        else
            users = userRepository.selectUsersIn(userIdList);

        List<UserResponseDto.Tier> response = new ArrayList<>();
        users.forEach(user -> {
            JsonElement element = getUserInformationByJsonElement(user.getBojId());
            int newUserTier = retrieveUserTier(element);

            // 변동이 있을 때만
            if (newUserTier != user.getUserTier()) {
                UserTierHistory tierHistory = UserTierHistory.builder()
                        .beforeTier(user.getUserTier())
                        .afterTier(newUserTier)
                        .user(user)
                        .build();
                tierHistoryRepository.save(tierHistory);
                user.changeTier(newUserTier);

                response.add(UserResponseDto.Tier.of(user));
            }
        });

        return response;
    }

    @Override
    public List<UserResponseDto.Status> updateUserStatus(List<UserRequestDto.Status> list) {
        List<Long> userIdList = new ArrayList<>();
        list.forEach(item -> userIdList.add(item.getUserId()));

        Map<Long, User> userMap = new HashMap<>();
        userRepository.findAllById(userIdList).forEach(user -> userMap.put(user.getId(), user));

        List<UserResponseDto.Status> response = new ArrayList<>();
        list.forEach(item -> {
            User user = userMap.get(item.getUserId());
            user.changeStatus(item.getStatus());

            response.add(UserResponseDto.Status.of(user));
        });

        return response;
    }

    @Override
    public ResponseEntity<?> login(Token.Request request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 사용자입니다.");
        });

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenService.generateToken(user.getId().toString(), user.getRole().toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(Token.Response.of(jwt), httpHeaders, HttpStatus.OK);
    }

    @Override
    public void resetPassword(User loginUser, UserRequestDto.PasswordReset pwData) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), pwData.getCurrentPassword());

        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        loginUser.changePassword(passwordEncoder.encode(pwData.getNewPassword()));

        userRepository.save(loginUser);
    }

    private void validateDuplicateUser(String bojId, String username) {
        userRepository.findByBojId(bojId)
                .ifPresent(m -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용중인 ID입니다.");
                });

        if (userRepository.findByUsername(username).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 User Id입니다.");
    }

    private int retrieveUserTier(JsonElement element) {
        return element
                .getAsJsonObject().get("items").getAsJsonArray().get(0)
                .getAsJsonObject().get("tier").getAsInt();
    }

    private boolean isBojUserExists(JsonElement element) {
        int count = element.getAsJsonObject().get("count").getAsInt();

        return (count == 1);
    }

    private JsonElement getUserInformationByJsonElement(String bojId) {
        String urlString = "https://solved.ac/api/v3/search/user?query=" + bojId;

        WebClient webClient = WebClient.create(urlString);

        String block = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (block == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID 정보를 찾을 수 없습니다.");

        return JsonParser.parseString(block);
    }
}
