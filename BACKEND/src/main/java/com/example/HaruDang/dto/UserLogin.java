package com.example.HaruDang.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLogin {
    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    @NotBlank
    @Size(max = 256)
    private String password;
}
