package com.example.HaruDang.service;

import com.example.HaruDang.entity.User;
import com.example.HaruDang.repository.UserRepository;
import com.example.HaruDang.security.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_NICKNAME = "testuser";
    private static final String USER_PASSWORD = "password";

    @DisplayName("사용자 ID로 UserDetails를 성공적으로 로드")
    @Test
    void loadUserByUsername_success() {
        // Given
        User mockUser = User.builder()
                .userId(USER_ID)
                .email(USER_EMAIL)
                .nickname(USER_NICKNAME)
                .password(USER_PASSWORD)
                .build();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(USER_ID));

        // Then
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertThat(customUserDetails.getUsername()).isEqualTo(USER_EMAIL);
        assertThat(customUserDetails.getUserId()).isEqualTo(USER_ID);
        assertThat(customUserDetails.getNickname()).isEqualTo(USER_NICKNAME);
        verify(userRepository, times(1)).findById(USER_ID);
    }

    @DisplayName("존재하지 않는 사용자 ID로 UserDetails 로드 시 예외 발생")
    @Test
    void loadUserByUsername_userNotFound_throwsUsernameNotFoundException() {
        // Given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername(String.valueOf(USER_ID)));

        assertThat(exception.getMessage()).contains("사용자를 찾을 수 없습니다" + USER_ID);
        verify(userRepository, times(1)).findById(USER_ID);
    }

    @DisplayName("유효하지 않은 사용자 ID 형식으로 UserDetails 로드 시 NumberFormatException 발생")
    @Test
    void loadUserByUsername_invalidUserIdFormat_throwsNumberFormatException() {
        // Given
        String invalidUserId = "abc";

        // When & Then
        assertThrows(NumberFormatException.class, () ->
                customUserDetailsService.loadUserByUsername(invalidUserId));

        verify(userRepository, never()).findById(anyLong()); // Ensure findById is not called
    }
}
