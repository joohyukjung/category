package com.musinsa.category.controller;

import com.musinsa.category.common.BaseControllerTest;
import com.musinsa.category.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest {

    @Test
    void signupSuccess() throws Exception {

        // Given
        UserDto userDto = new UserDto("test", "test@musinsa.com", "1234");

        // When
        ResultActions resultActions = this.mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDto))
        );

        // Then
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("name").value("test"))
                .andExpect(jsonPath("email").value("test@musinsa.com"));
    }
}