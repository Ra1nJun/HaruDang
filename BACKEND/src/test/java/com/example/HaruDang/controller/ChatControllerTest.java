package com.example.HaruDang.controller;

import com.example.HaruDang.dto.ChatRequest;
import com.example.HaruDang.dto.ChatResponse;
import com.example.HaruDang.entity.User;
import com.example.HaruDang.security.CustomUserDetails;
import com.example.HaruDang.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_NICKNAME = "testuser";
    private static final String USER_PASSWORD = "password";
    private static final String CHAT_MESSAGE = "안녕하세요.";
    private static final String AI_ANSWER = "반갑습니다. 무엇을 도와드릴까요?";

    @DisplayName("인증된 사용자의 챗 메시지 처리")
    @Test
    @WithMockUser(username = USER_EMAIL, roles = {"USER"})
    void chat_authenticatedUser_returnsChatResponse() throws Exception {
        // Mock authenticated user details
        CustomUserDetails customUserDetails = new CustomUserDetails(User.builder()
                .userId(USER_ID)
                .email(USER_EMAIL)
                .nickname(USER_NICKNAME)
                .password(USER_PASSWORD)
                .build());

        // Set up SecurityContextHolder with the mocked user
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        ChatRequest chatRequest = new ChatRequest(CHAT_MESSAGE);
        when(chatService.getAnswer(CHAT_MESSAGE)).thenReturn(AI_ANSWER);

        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(String.valueOf(USER_ID)))
                .andExpect(jsonPath("$.answer").value(AI_ANSWER));
    }

    @DisplayName("익명 사용자의 챗 메시지 처리")
    @Test
    void chat_anonymousUser_returnsChatResponse() throws Exception {
        // Ensure no authenticated user in security context
        SecurityContextHolder.clearContext();

        ChatRequest chatRequest = new ChatRequest(CHAT_MESSAGE);
        when(chatService.getAnswer(CHAT_MESSAGE)).thenReturn(AI_ANSWER);

        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("anonymous"))
                .andExpect(jsonPath("$.answer").value(AI_ANSWER));
    }

    @DisplayName("유효하지 않은 챗 메시지로 요청 시 BAD_REQUEST 반환")
    @Test
    void chat_invalidMessage_returnsBadRequest() throws Exception {
        ChatRequest chatRequest = new ChatRequest(""); // Empty message, should be invalid

        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isBadRequest()); // Expecting validation error
    }
}
