package com.musinsa.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotNull
    private String value;

    @NotNull
    private Long parentId;

    private List childNodes;

    public void setChildNodes(List childNodes) {
        this.childNodes = childNodes;
    }

    public static CategoryDto from(Category category) {
        if (category == null) return null;

        return CategoryDto.builder()
                .id(category.getId())
                .value(category.getValue())
                .build();
    }
}
