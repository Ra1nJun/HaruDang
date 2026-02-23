package com.example.HaruDang.controller;

import com.example.HaruDang.dto.UserCreate;
import com.example.HaruDang.exception.BusinessException;
import com.example.HaruDang.exception.ErrorCode;
import com.example.HaruDang.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_NICKNAME = "testuser";
    private static final String USER_PASSWORD = "Password123!";

    @DisplayName("사용자 등록 성공")
    @Test
    void register_success() throws Exception {
        UserCreate userCreate = new UserCreate();
        userCreate.setEmail(USER_EMAIL);
        userCreate.setNickname(USER_NICKNAME);
        userCreate.setPassword(USER_PASSWORD);

        doNothing().when(userService).createUser(any(UserCreate.class));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registered successfully"));

        verify(userService, times(1)).createUser(any(UserCreate.class));
    }

    @DisplayName("사용자 등록 실패 - 중복 이메일")
    @Test
    void register_duplicateEmail_returnsBadRequest() throws Exception {
        UserCreate userCreate = new UserCreate();
        userCreate.setEmail(USER_EMAIL);
        userCreate.setNickname(USER_NICKNAME);
        userCreate.setPassword(USER_PASSWORD);

        doThrow(new BusinessException(ErrorCode.DUPLICATE_EMAIL))
                .when(userService).createUser(any(UserCreate.class));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("U002")); // Assuming U002 is DUPLICATE_EMAIL

        verify(userService, times(1)).createUser(any(UserCreate.class));
    }

    @DisplayName("사용자 등록 실패 - 유효하지 않은 입력 값 (이메일 형식)")
    @Test
    void register_invalidEmailFormat_returnsBadRequest() throws Exception {
        UserCreate userCreate = new UserCreate();
        userCreate.setEmail("invalid-email"); // Invalid email format
        userCreate.setNickname(USER_NICKNAME);
        userCreate.setPassword(USER_PASSWORD);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreate.class));
    }

    @DisplayName("사용자 등록 실패 - 유효하지 않은 입력 값 (비밀번호 형식)")
    @Test
    void register_invalidPasswordFormat_returnsBadRequest() throws Exception {
        UserCreate userCreate = new UserCreate();
        userCreate.setEmail(USER_EMAIL);
        userCreate.setNickname(USER_NICKNAME);
        userCreate.setPassword("short"); // Invalid password format

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreate.class));
    }

    @DisplayName("사용자 등록 실패 - 유효하지 않은 입력 값 (닉네임 비어있음)")
    @Test
    void register_emptyNickname_returnsBadRequest() throws Exception {
        UserCreate userCreate = new UserCreate();
        userCreate.setEmail(USER_EMAIL);
        userCreate.setNickname(""); // Empty nickname
        userCreate.setPassword(USER_PASSWORD);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreate.class));
    }
}
