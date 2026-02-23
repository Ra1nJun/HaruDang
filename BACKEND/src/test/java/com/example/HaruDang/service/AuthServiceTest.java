package com.example.HaruDang.service;

import com.example.HaruDang.dto.TokenDto;
import com.example.HaruDang.entity.RefreshToken;
import com.example.HaruDang.exception.BusinessException;
import com.example.HaruDang.exception.ErrorCode;
import com.example.HaruDang.repository.RefreshTokenRepository;
import com.example.HaruDang.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    private static final String OLD_REFRESH_TOKEN = "oldRefreshToken";
    private static final String NEW_ACCESS_TOKEN = "newAccessToken";
    private static final String NEW_REFRESH_TOKEN = "newRefreshToken";
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test if necessary, though @Mock handles some of this.
        // It's good practice for clarity or complex interactions.
    }

    @DisplayName("리프레시 토큰 재발급 성공")
    @Test
    void reissue_success() {
        // Given
        RefreshToken savedRefreshToken = new RefreshToken(USER_ID, OLD_REFRESH_TOKEN);

        when(jwtTokenProvider.validateToken(OLD_REFRESH_TOKEN)).thenReturn(true);
        when(refreshTokenRepository.findByToken(OLD_REFRESH_TOKEN)).thenReturn(Optional.of(savedRefreshToken));
        when(jwtTokenProvider.createAccessToken(USER_ID)).thenReturn(NEW_ACCESS_TOKEN);
        when(jwtTokenProvider.createRefreshToken(USER_ID)).thenReturn(NEW_REFRESH_TOKEN);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(savedRefreshToken);

        // When
        TokenDto result = authService.reissue(OLD_REFRESH_TOKEN);

        // Then
        assertThat(result.getAccessToken()).isEqualTo(NEW_ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(NEW_REFRESH_TOKEN);
        assertThat(savedRefreshToken.getToken()).isEqualTo(NEW_REFRESH_TOKEN); // Verify token was updated

        verify(jwtTokenProvider, times(1)).validateToken(OLD_REFRESH_TOKEN);
        verify(refreshTokenRepository, times(1)).findByToken(OLD_REFRESH_TOKEN);
        verify(jwtTokenProvider, times(1)).createAccessToken(USER_ID);
        verify(jwtTokenProvider, times(1)).createRefreshToken(USER_ID);
        verify(refreshTokenRepository, times(1)).save(savedRefreshToken);
    }

    @DisplayName("유효하지 않은 리프레시 토큰으로 재발급 시도 시 예외 발생")
    @Test
    void reissue_invalidToken_throwsBusinessException() {
        // Given
        when(jwtTokenProvider.validateToken(OLD_REFRESH_TOKEN)).thenReturn(false);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () ->
                authService.reissue(OLD_REFRESH_TOKEN));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_TOKEN);
        verify(jwtTokenProvider, times(1)).validateToken(OLD_REFRESH_TOKEN);
        verify(refreshTokenRepository, never()).findByToken(anyString());
    }

    @DisplayName("존재하지 않는 리프레시 토큰으로 재발급 시도 시 예외 발생")
    @Test
    void reissue_tokenNotFound_throwsBusinessException() {
        // Given
        when(jwtTokenProvider.validateToken(OLD_REFRESH_TOKEN)).thenReturn(true);
        when(refreshTokenRepository.findByToken(OLD_REFRESH_TOKEN)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () ->
                authService.reissue(OLD_REFRESH_TOKEN));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_TOKEN);
        verify(jwtTokenProvider, times(1)).validateToken(OLD_REFRESH_TOKEN);
        verify(refreshTokenRepository, times(1)).findByToken(OLD_REFRESH_TOKEN);
        verify(jwtTokenProvider, never()).createAccessToken(anyLong());
        verify(jwtTokenProvider, never()).createRefreshToken(anyLong());
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }
}
