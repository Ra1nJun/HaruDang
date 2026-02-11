package com.example.HaruDang.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class ChatService {
    private final WebClient webClient;

    public ChatService(WebClient.Builder webClientBuilder,
                       @Value("http://${langserve.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public String getAnswer(String query) {
        // 1. 요청 준비: ChatRequest 대신 Map을 사용하여 LangServe 구조 생성
        Map<String, Object> body = Map.of(
                "input", Map.of(
                        "question", query,
                        "one_way", true
                )
        );

        try {
            // 2. 호출 및 응답 처리
            var response = webClient.post()
                    .uri("/chat/invoke")
                    .bodyValue(body)
                    .retrieve()
                    // 응답의 "output" 내부에 우리가 원하는 answer가 들어있음을 명시
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Map<String, Object>>>() {})
                    .block();

            if (response != null && response.containsKey("output")) {
                Map<String, Object> output = response.get("output");
                return String.valueOf(output.get("answer"));
            }
        } catch (Exception e) {
            log.error("LangServe 통신 에러: {}", e.getMessage());
        }
        return "답변을 가져오지 못했습니다.";
    }
}