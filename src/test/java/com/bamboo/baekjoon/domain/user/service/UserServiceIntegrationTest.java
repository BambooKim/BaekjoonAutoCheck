package com.bamboo.baekjoon.domain.user.service;

import com.bamboo.baekjoon.domain.user.Users;
import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("새 User 저장 - 정상케이스")
    public void createNewUser() {
        // given
        UserRequestDto.Creation creationDto = UserRequestDto.Creation.builder()
                .korName("김범구")
                .enterYear(2018)
                .bojId("rlaqjarn1008")
                .build();

        // when
        UserResponseDto.Creation createdDto = userService.createUser(creationDto);
        Users findUser = userRepository.findByBojId(creationDto.getBojId()).orElse(null);

        // then
        assertThat(findUser).isNotNull();
        assertThat(createdDto.getId()).isEqualTo(findUser.getId());
        assertThat(createdDto.getKorName()).isEqualTo(findUser.getKorName());
        assertThat(createdDto.getUserTier()).isEqualTo(findUser.getUserTier());
        assertThat(createdDto.getEnterYear()).isEqualTo(findUser.getEnterYear());
        assertThat(createdDto.getBojId()).isEqualTo(findUser.getBojId());
        assertThat(createdDto.getStatus()).isEqualTo(findUser.getStatus());
        assertThat(createdDto.getJoinedAt()).isEqualTo(findUser.getJoinedAt());

    }

    @Test
    @DisplayName("중복 회원 저장")
    public void createDuplicateUser() {
        // given
        UserRequestDto.Creation creationDto1 = UserRequestDto.Creation.builder()
                .korName("김범구")
                .enterYear(2018)
                .bojId("rlaqjarn1008")
                .build();

        UserRequestDto.Creation creationDto2 = UserRequestDto.Creation.builder()
                .korName("이은찬")
                .enterYear(2018)
                .bojId("rlaqjarn1008")
                .build();

        // when
        UserResponseDto.Creation createdDto1 = userService.createUser(creationDto1);

        // then
        assertThrows(ResponseStatusException.class, () -> {
            UserResponseDto.Creation createdDto2 = userService.createUser(creationDto2);
        });
    }

    @Test
    @DisplayName("없는 ID 사용")
    public void createNoneUser() {
        // given
        UserRequestDto.Creation creationDto1 = UserRequestDto.Creation.builder()
                .korName("김범구")
                .enterYear(2018)
                .bojId("asdfqwerzxcvasdf")
                .build();

        // when

        // then
        assertThrows(ResponseStatusException.class, () -> {
            UserResponseDto.Creation createdDto2 = userService.createUser(creationDto1);
        });
    }
}