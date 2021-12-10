package com.musinsa.category.controller;

import com.musinsa.category.dto.CategoryDto;
import com.musinsa.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 전체 categories 조회
     * @return  계층구조 categoris
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * 해당 id에 해당하는 category 조회 (하위 categories 포함)
     * @param id    조회할 category id
     * @return      해당 id의 계층구조 categoris
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    /**
     * 카테고리 생성
     * @param categoryDto   category 생성 정보
     * @return              생성된 category
     */
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    /**
     * 해당 id에 해당하는 category 수정
     * @param id            수정할 category id
     * @param categoryDto   category 수정 정보
     * @return              수정된 category
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }

    /**
     * 해당 id에 해당하는 category 삭제 (하위 categories 포함)
     * @param id    삭제할 category id
     * @return      200
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 전체 categories 조회 (비계층구조, 전체나열)
     * @return  비계층구조 categoris
     */
    @GetMapping("/categories-not-hierarchy")
    public ResponseEntity<List<CategoryDto>> getCategoriesNotHierarchy() {
        return ResponseEntity.ok(categoryService.getAllCategoriesNotHierarchy());
    }
}
