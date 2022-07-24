package com.bamboo.baekjoon.domain.user.service;

import com.bamboo.baekjoon.domain.user.UserStatus;
import com.bamboo.baekjoon.domain.user.Users;
import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto.Creation createUser(UserRequestDto.Creation createUserData) {

        validateDuplicateUser(createUserData.getBojId());

        JsonElement element = getUserInformationByJsonElement(createUserData.getBojId());

        if (!isBojUserExists(element))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 백준 아이디를 입력해주세요.");

        int userTier = retrieveUserTier(element);

        Users user = Users.builder()
                .korName(createUserData.getKorName())
                .userTier(userTier)
                .enterYear(createUserData.getEnterYear())
                .bojId(createUserData.getBojId())
                .status(UserStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return UserResponseDto.Creation.of(user);
    }

    private void validateDuplicateUser(String bojId) {
        userRepository.findByBojId(bojId)
                .ifPresent(m -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용중인 ID입니다.");
                });
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
