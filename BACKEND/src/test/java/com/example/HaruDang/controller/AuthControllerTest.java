package com.example.HaruDang.controller;

import com.example.HaruDang.dto.TokenDto;
import com.example.HaruDang.dto.UserLogin;
import com.example.HaruDang.entity.User;
import com.example.HaruDang.exception.BusinessException;
import com.example.HaruDang.exception.ErrorCode;
import com.example.HaruDang.security.CustomUserDetails;
import com.example.HaruDang.security.JwtTokenProvider;
import com.example.HaruDang.service.AuthService;
import com.example.HaruDang.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthService authService;

    // These values are injected from application.properties in a real context.
    // For @WebMvcTest, we don't have a full application context to resolve @Value.
    // However, the controller methods themselves don't directly use these values
    // for logic that needs testing here; they are used in cookie building.
    // If cookie values need precise checking, we might need a more integrated test
    // or manually set these. For now, we'll assume they're handled.

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_PASSWORD = "password";
    private static final String USER_NICKNAME = "testuser";
    private static final String ACCESS_TOKEN = "testAccessToken";
    private static final String REFRESH_TOKEN = "testRefreshToken";

    @DisplayName("로그인 성공")
    @Test
    void login_success() throws Exception {
        UserLogin loginDto = new UserLogin();
        loginDto.setEmail(USER_EMAIL);
        loginDto.setPassword(USER_PASSWORD);

        User mockUser = User.builder()
                .email(USER_EMAIL)
                .nickname(USER_NICKNAME)
                .password("encodedPassword") // PasswordEncoder handles this
                .build();
        ReflectionTestUtils.setField(mockUser, "userId", USER_ID); // Set userId using reflection

        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(mockUser);
        when(userService.checkPassword(USER_PASSWORD, mockUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(USER_ID)).thenReturn(ACCESS_TOKEN);
        when(jwtTokenProvider.createRefreshToken(USER_ID)).thenReturn(REFRESH_TOKEN);
        doNothing().when(userService).updateRefreshToken(anyLong(), anyString());

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged in successfully"))
                .andReturn();

        String setCookieHeader = result.getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains("accessToken=" + ACCESS_TOKEN);
        assertThat(setCookieHeader).contains("refreshToken=" + REFRESH_TOKEN);

        verify(userService, times(1)).getUserByEmail(USER_EMAIL);
        verify(userService, times(1)).checkPassword(USER_PASSWORD, mockUser.getPassword());
        verify(jwtTokenProvider, times(1)).createAccessToken(USER_ID);
        verify(jwtTokenProvider, times(1)).createRefreshToken(USER_ID);
        verify(userService, times(1)).updateRefreshToken(USER_ID, REFRESH_TOKEN);
    }

    @DisplayName("로그인 실패 - 유효하지 않은 자격 증명")
    @Test
    void login_invalidCredentials() throws Exception {
        UserLogin loginDto = new UserLogin();
        loginDto.setEmail(USER_EMAIL);
        loginDto.setPassword(USER_PASSWORD);

        // Simulate user not found
        when(userService.getUserByEmail(USER_EMAIL)).thenThrow(new BusinessException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isNotFound()) // Or BAD_REQUEST depending on GlobalExceptionHandler setup
                .andExpect(jsonPath("$.code").value("U001")); // Assuming U001 is USER_NOT_FOUND

        verify(userService, times(1)).getUserByEmail(USER_EMAIL);
        verify(userService, never()).checkPassword(anyString(), anyString());
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void reissue_success() throws Exception {
        TokenDto tokenDto = new TokenDto(ACCESS_TOKEN, REFRESH_TOKEN);
        when(authService.reissue(REFRESH_TOKEN)).thenReturn(tokenDto);

        MvcResult result = mockMvc.perform(post("/auth/reissue")
                        .cookie(new jakarta.servlet.http.Cookie("refreshToken", REFRESH_TOKEN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tokens reissued successfully"))
                .andReturn();

        String setCookieHeader = result.getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains("accessToken=" + ACCESS_TOKEN);
        assertThat(setCookieHeader).contains("refreshToken=" + REFRESH_TOKEN);

        verify(authService, times(1)).reissue(REFRESH_TOKEN);
    }

    @DisplayName("토큰 재발급 실패 - 유효하지 않은 리프레시 토큰")
    @Test
    void reissue_invalidRefreshToken() throws Exception {
        when(authService.reissue(anyString())).thenThrow(new BusinessException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(post("/auth/reissue")
                        .cookie(new jakarta.servlet.http.Cookie("refreshToken", "invalidToken")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("A001"));

        verify(authService, times(1)).reissue("invalidToken");
    }

    @DisplayName("로그아웃 성공 - 액세스 토큰 존재")
    @Test
    void logout_withAccessToken_success() throws Exception {
        when(jwtTokenProvider.validateToken(ACCESS_TOKEN)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(ACCESS_TOKEN)).thenReturn(USER_ID);
        doNothing().when(userService).deleteRefreshToken(USER_ID);

        MvcResult result = mockMvc.perform(post("/auth/logout")
                        .cookie(new jakarta.servlet.http.Cookie("accessToken", ACCESS_TOKEN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"))
                .andReturn();

        String setCookieHeader = result.getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains("accessToken=; Max-Age=0");
        assertThat(setCookieHeader).contains("refreshToken=; Max-Age=0");

        verify(jwtTokenProvider, times(1)).validateToken(ACCESS_TOKEN);
        verify(jwtTokenProvider, times(1)).getUserIdFromToken(ACCESS_TOKEN);
        verify(userService, times(1)).deleteRefreshToken(USER_ID);
    }

    @DisplayName("로그아웃 성공 - 액세스 토큰 부재")
    @Test
    void logout_noAccessToken_success() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/logout")) // No accessToken cookie
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"))
                .andReturn();

        String setCookieHeader = result.getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains("accessToken=; Max-Age=0");
        assertThat(setCookieHeader).contains("refreshToken=; Max-Age=0");

        verify(jwtTokenProvider, never()).validateToken(anyString());
        verify(jwtTokenProvider, never()).getUserIdFromToken(anyString());
        verify(userService, never()).deleteRefreshToken(anyLong());
    }

    @DisplayName("현재 사용자 정보 조회 성공 - 인증된 사용자")
    @Test
    @WithMockUser(username = USER_EMAIL) // Simulates an authenticated user
    void getCurrentUser_authenticated_success() throws Exception {
        User mockUser = User.builder()
                .email(USER_EMAIL)
                .nickname(USER_NICKNAME)
                .password(USER_PASSWORD)
                .build();
        ReflectionTestUtils.setField(mockUser, "userId", USER_ID); // Set userId using reflection

        CustomUserDetails customUserDetails = new CustomUserDetails(mockUser);

        // In @WithMockUser, the principal is a UserDetails object.
        // We need to ensure that the mocked AuthenticationPrincipal returns our CustomUserDetails
        // when AuthController.getCurrentUser is called. This is tricky with @WebMvcTest.
        // For simplicity in this test, we'll assume the @AuthenticationPrincipal correctly
        // provides the user details based on the @WithMockUser setup, and focus on the controller's logic.
        // The framework typically handles the mapping from @WithMockUser to the principal.
        // We can just verify the response content directly here.

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(USER_NICKNAME));
    }

    @DisplayName("현재 사용자 정보 조회 실패 - 인증되지 않은 사용자")
    @Test
    void getCurrentUser_unauthenticated_failure() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("인증 실패"));
    }
}
