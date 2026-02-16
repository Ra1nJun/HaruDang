package com.example.HaruDang.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.example.HaruDang.dto.ChatRequest;
import com.example.HaruDang.dto.ChatResponse;
import com.example.HaruDang.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.HaruDang.security.CustomUserDetails;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId;

        if (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            currentUserId = "anonymous"; // Or generate a unique ID for anonymous sessions if needed
        } else {
            // Logged-in user: get ID from CustomUserDetails
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            currentUserId = String.valueOf(userDetails.getUserId());
        }

        String aiAnswer = chatService.getAnswer(request.getMessage());

        return ChatResponse.of(currentUserId, aiAnswer);
    }
}
