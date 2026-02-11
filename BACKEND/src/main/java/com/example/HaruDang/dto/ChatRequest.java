package com.example.HaruDang.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequest {

    @NotBlank(message = "메세지가 비어있습니다.")
    private String message;
}
