package com.example.HaruDang.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCreate {
    @NotBlank(message = "사용자 이름이 비어있습니다.")
    @Size(min = 1, max = 20)
    private String nickname;

    @NotBlank(message = "이메일이 비어있습니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    @Size(max = 128)
    private String email;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자 이상 & 영문, 숫자, 특수문자를 최소 하나씩 포함해야 합니다."
    )
    @Size(max = 256)
    private String password;
}
