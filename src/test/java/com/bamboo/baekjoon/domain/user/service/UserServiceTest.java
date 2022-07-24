package com.bamboo.baekjoon.domain.user.service;

import com.bamboo.baekjoon.domain.user.Users;
import com.bamboo.baekjoon.domain.user.dto.UserRequestDto;
import com.bamboo.baekjoon.domain.user.dto.UserResponseDto;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("새 User 저장 - 정상케이스")
    public void createNewUser() {
        // given
        UserRequestDto.Creation creationDto = UserRequestDto.Creation.builder()
                .korName("김범구")
                .enterYear(2018)
                .bojId("rlaqjarn1008")
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.empty());
        given(userRepository.save(argThat(user -> user.getBojId().equals("rlaqjarn1008"))))
                .willReturn(Users.builder()
                        .korName(creationDto.getKorName())
                        .enterYear(creationDto.getEnterYear())
                        .bojId(creationDto.getBojId())
                        .build());

        // when
        UserResponseDto.Creation createdDto = userService.createUser(creationDto);

        // then
        verify(userRepository).save(argThat(user -> user.getBojId().equals("rlaqjarn1008")));
    }
}