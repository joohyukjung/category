package com.musinsa.category.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class AuthDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
