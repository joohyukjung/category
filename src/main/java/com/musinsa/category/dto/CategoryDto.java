package com.musinsa.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    private List<CategoryDto> childNodes;

    public void setChildNodes(List<CategoryDto> childNodes) {
        this.childNodes = childNodes;
    }

    public static CategoryDto from(Category category) {
        if (category == null) return null;

        return CategoryDto.builder()
                .id(category.getId())
                .value(category.getValue())
                .build();
    }

    public static CategoryDto from2(Category category) {
        if (category == null) return null;

        return CategoryDto.builder()
                .id(category.getId())
                .value(category.getValue())
                .parentId(category.getParentId())
                .childNodes(new ArrayList())
                .build();
    }
}
