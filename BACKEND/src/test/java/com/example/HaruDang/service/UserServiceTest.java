package com.example.HaruDang.service;

import com.example.HaruDang.dto.UserCreate;
import com.example.HaruDang.entity.RefreshToken;
import com.example.HaruDang.entity.User;
import com.example.HaruDang.exception.BusinessException;
import com.example.HaruDang.exception.ErrorCode;
import com.example.HaruDang.repository.RefreshTokenRepository;
import com.example.HaruDang.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private UserService userService;

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_NICKNAME = "testuser";
    private static final String RAW_PASSWORD = "rawPassword123!";
    private static final String ENCODED_PASSWORD = "encodedPassword123!";
    private static final String REFRESH_TOKEN_STRING = "refreshTokenString";

    @DisplayName("이메일로 사용자 조회 성공")
    @Test
    void getUserByEmail_success() {
        // Given
        User mockUser = User.builder().email(USER_EMAIL).nickname(USER_NICKNAME).password(ENCODED_PASSWORD).build();
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));

        // When
        User foundUser = userService.getUserByEmail(USER_EMAIL);

        // Then
        assertThat(foundUser.getEmail()).isEqualTo(USER_EMAIL);
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
    }

    @DisplayName("존재하지 않는 이메일로 사용자 조회 시 예외 발생")
    @Test
    void getUserByEmail_userNotFound_throwsBusinessException() {
        // Given
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () ->
                userService.getUserByEmail(USER_EMAIL));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
    }

    @DisplayName("새로운 사용자 생성 성공")
    @Test
    void createUser_success() {
        // Given
        UserCreate userCreateDto = new UserCreate();
        userCreateDto.setEmail(USER_EMAIL);
        userCreateDto.setNickname(USER_NICKNAME);
        userCreateDto.setPassword(RAW_PASSWORD);

        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved user

        // When
        userService.createUser(userCreateDto);

        // Then
        verify(userRepository, times(1)).existsByEmail(USER_EMAIL);
        verify(passwordEncoder, times(1)).encode(RAW_PASSWORD);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("중복된 이메일로 사용자 생성 시 예외 발생")
    @Test
    void createUser_duplicateEmail_throwsBusinessException() {
        // Given
        UserCreate userCreateDto = new UserCreate();
        userCreateDto.setEmail(USER_EMAIL);
        userCreateDto.setNickname(USER_NICKNAME);
        userCreateDto.setPassword(RAW_PASSWORD);

        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () ->
                userService.createUser(userCreateDto));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_EMAIL);
        verify(userRepository, times(1)).existsByEmail(USER_EMAIL);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("비밀번호 일치 확인 성공")
    @Test
    void checkPassword_matches_returnsTrue() {
        // Given
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        // When
        boolean result = userService.checkPassword(RAW_PASSWORD, ENCODED_PASSWORD);

        // Then
        assertThat(result).isTrue();
        verify(passwordEncoder, times(1)).matches(RAW_PASSWORD, ENCODED_PASSWORD);
    }

    @DisplayName("비밀번호 불일치 확인 성공")
    @Test
    void checkPassword_notMatches_returnsFalse() {
        // Given
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        // When
        boolean result = userService.checkPassword(RAW_PASSWORD, ENCODED_PASSWORD);

        // Then
        assertThat(result).isFalse();
        verify(passwordEncoder, times(1)).matches(RAW_PASSWORD, ENCODED_PASSWORD);
    }

    @DisplayName("리프레시 토큰 업데이트 또는 저장 성공 - 기존 토큰 존재")
    @Test
    void updateRefreshToken_existingToken_success() {
        // Given
        RefreshToken existingToken = new RefreshToken(USER_ID, "oldRefreshToken");
        when(refreshTokenRepository.findById(USER_ID)).thenReturn(Optional.of(existingToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(existingToken);

        // When
        userService.updateRefreshToken(USER_ID, REFRESH_TOKEN_STRING);

        // Then
        assertThat(existingToken.getToken()).isEqualTo(REFRESH_TOKEN_STRING);
        verify(refreshTokenRepository, times(1)).findById(USER_ID);
        verify(refreshTokenRepository, times(1)).save(existingToken);
    }

    @DisplayName("리프레시 토큰 업데이트 또는 저장 성공 - 새로운 토큰")
    @Test
    void updateRefreshToken_newToken_success() {
        // Given
        when(refreshTokenRepository.findById(USER_ID)).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
            RefreshToken savedToken = invocation.getArgument(0);
            assertThat(savedToken.getUserId()).isEqualTo(USER_ID);
            assertThat(savedToken.getToken()).isEqualTo(REFRESH_TOKEN_STRING);
            return savedToken;
        });

        // When
        userService.updateRefreshToken(USER_ID, REFRESH_TOKEN_STRING);

        // Then
        verify(refreshTokenRepository, times(1)).findById(USER_ID);
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @DisplayName("리프레시 토큰 삭제 성공")
    @Test
    void deleteRefreshToken_success() {
        // Given - no specific setup needed for deleteById
        doNothing().when(refreshTokenRepository).deleteById(USER_ID);

        // When
        userService.deleteRefreshToken(USER_ID);

        // Then
        verify(refreshTokenRepository, times(1)).deleteById(USER_ID);
    }
}
