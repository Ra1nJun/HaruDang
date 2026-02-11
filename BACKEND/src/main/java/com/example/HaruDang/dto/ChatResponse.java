package com.example.HaruDang.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 빈 객체 생성 방지
@AllArgsConstructor
public class ChatResponse {
    private String userId;
    private String answer;

    public static ChatResponse of(String userId, String answer) {
        return ChatResponse.builder()
                .userId(userId)
                .answer(answer)
                .build();
    }
}
