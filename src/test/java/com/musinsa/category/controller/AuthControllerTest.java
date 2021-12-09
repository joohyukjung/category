package com.musinsa.category.controller;

import com.musinsa.category.common.BaseControllerTest;
import com.musinsa.category.dto.AuthDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseControllerTest {

    @Test
    void authorizeSuccess() throws Exception {

        // Given
        AuthDto authDto = new AuthDto("admin@musinsa.com", "1234");

        // When
        ResultActions resultActions = this.mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(authDto))
        );

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    void authorizeFailBecausePasswordIsInValid() throws Exception {

        // Given
        AuthDto authDto = new AuthDto("admin@musinsa.com", "4321");

        // When
        ResultActions resultActions = this.mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(authDto))
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    void authorizeFailBecausePasswordIsNull() throws Exception {

        // Given
        AuthDto authDto = new AuthDto("admin@musinsa.com", null);

        // When
        ResultActions resultActions = this.mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(authDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}