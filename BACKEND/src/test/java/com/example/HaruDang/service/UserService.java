package com.example.HaruDang.service;

import com.example.HaruDang.entity.RefreshToken;
import com.example.HaruDang.entity.User;
import com.example.HaruDang.dto.UserCreate;
import com.example.HaruDang.exception.BusinessException;
import com.example.HaruDang.exception.ErrorCode;
import com.example.HaruDang.repository.RefreshTokenRepository;
import com.example.HaruDang.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void createUser(UserCreate userDto) {
        validateDuplicateEmail(userDto.getEmail());

        String rawPassword = userDto.getPassword();
        String hashedPW = passwordEncoder.encode(rawPassword);

        User dbUser = User.builder()
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .password(hashedPW)
                .build();

        userRepository.save(dbUser);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public void updateRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .map(t -> {
                    t.updateToken(token);
                    return t;
                })
                .orElse(new RefreshToken(userId, token));

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
