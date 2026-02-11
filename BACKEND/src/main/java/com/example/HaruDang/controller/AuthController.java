package com.example.HaruDang.controller;

import com.example.HaruDang.dto.TokenDto;
import com.example.HaruDang.entity.User;
import com.example.HaruDang.dto.UserLogin;
import com.example.HaruDang.exception.BusinessException;
import com.example.HaruDang.exception.ErrorCode;
import com.example.HaruDang.security.CustomUserDetails;
import com.example.HaruDang.security.JwtTokenProvider;
import com.example.HaruDang.service.AuthService;
import com.example.HaruDang.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    @Value("${jwt.expiration}") int accessExpireTime;
    @Value("${jwt.refresh}") long refreshExpireTime;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin loginDto, HttpServletResponse response){
        User user = userService.getUserByEmail(loginDto.getEmail());
        if (user == null || !userService.checkPassword(loginDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        userService.updateRefreshToken(user.getUserId(), refreshToken);

        // Access Token 쿠키 설정
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(accessExpireTime)
                .sameSite("Strict")
                .build();

        // Refresh Token 쿠키 설정
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth")
                .maxAge(refreshExpireTime)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged in successfully"));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        TokenDto tokenDto = authService.reissue(refreshToken);

        // Access Token 쿠키 설정
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokenDto.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(accessExpireTime)
                .sameSite("Strict")
                .build();

        // Refresh Token 쿠키 설정
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshExpireTime)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Tokens reissued successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            HttpServletResponse response) {

        // DB에서 리프레시 토큰 삭제
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
            userService.deleteRefreshToken(userId);
        }

        // 쿠키 삭제
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", null)
                .path("/")
                .maxAge(0)
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", null)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }

        return ResponseEntity.ok(Map.of(
                "nickname", userDetails.getNickname()
        ));
    }
}
