package com.musinsa.category.dto;

import lombok.Getter;

@Getter
public class TokenDto {
    private String token;

    public TokenDto(String jwt) {
        this.token = jwt;
    }
}
