package com.musinsa.category.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.musinsa.category.common.BaseControllerTest;
import com.musinsa.category.dto.AuthDto;
import com.musinsa.category.dto.CategoryDto;
import com.musinsa.category.entity.Category;
import com.musinsa.category.repository.CategoryRepository;
import com.musinsa.category.service.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class CategoryControllerTest extends BaseControllerTest {

    private String token;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setAuthorization() throws Exception {
        AuthDto authDto = new AuthDto("admin@musinsa.com", "1234");

        ResultActions resultActions = this.mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(authDto))
        );

        String result = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(result);
        token = "Bearer " + node.path("token").asText();
    }

    @Test
    @DisplayName("GET - 전체 categories 조회")
    @Order(1)
    void getCategories() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        );

        // Then
        resultActions.andExpect(status().is2xxSuccessful())
                // [{""value":"의류"}, {"value":"바지"}, {"value":"신발"}, {"value":"뷰티"}]
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    @DisplayName("GET - 해당 category 조회")
    @Order(2)
    void getCategory() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(get("/categories/15")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        );

        // Then
        resultActions.andExpect(status().is2xxSuccessful())
                // {"id:15", "value":"뷰티"}
                .andExpect(jsonPath("value").value("뷰티"));
    }

    @Test
    @DisplayName("POST - category 생성")
    @Order(3)
    void createCategory() throws Exception {
        // Given
        CategoryDto categoryDto = new CategoryDto(null, "가방", 0L, null);

        // When
        ResultActions resultActions = this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(this.objectMapper.writeValueAsString(categoryDto))
        );

        // Then
        resultActions.andExpect(status().is2xxSuccessful())
                // {"id:16", "value":"가방"}
                .andExpect(jsonPath("value").value("가방"));
    }

    @Test
    @DisplayName("POST - category 수정")
    @Order(4)
    void updateCategory() throws Exception {
        // Given
        Category category = Category.builder()
                .id(null)
                .value("test")
                .parentId(0L)
                .build();
        category = categoryRepository.save(category);

        CategoryDto categoryDto = new CategoryDto(null, "레깅스", 7L, null);

        // When
        ResultActions resultActions = this.mockMvc.perform(put("/categories/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(this.objectMapper.writeValueAsString(categoryDto))
        );

        // Then
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("value").value("레깅스"));
    }

    @Test
    @DisplayName("DELETE - category 삭제")
    @Order(5)
    void deleteCategory() throws Exception {
        // Given
        Category category = Category.builder()
                .id(null)
                .value("test")
                .parentId(0L)
                .build();
        category = categoryRepository.save(category);

        // When
        ResultActions resultActions = this.mockMvc.perform(delete("/categories/"+category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        );

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }
}