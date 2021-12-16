package com.musinsa.category.controller;

import com.musinsa.category.dto.CategoryDto;
import com.musinsa.category.service.CategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "전체 카테고리 조회", notes = "전체 categories 조회")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @ApiOperation(value = "전체 카테고리 조회 v2", notes = "빈번한 DB Connection의 부하를 줄이기 위하여, 서버에서 계층구조 처리함")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/v2/categories")
    public ResponseEntity<List<CategoryDto>> getCategoriesV2() {
        return ResponseEntity.ok(categoryService.getAllCategoriesV2());
    }

    @ApiOperation(value = "카테고리 조회", notes = "해당 id에 해당하는 category 조회 (하위 categories 포함)")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @ApiOperation(value = "카테고리 생성", notes = "카테고리 생성")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @ApiOperation(value = "카테고리 수정", notes = "해당 id에 해당하는 category 수정")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }

    @ApiOperation(value = "카테고리 삭제", notes = "해당 id에 해당하는 category 삭제 (하위 categories 포함)")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "전체 카테고리 조회 v3", notes = "비계층구조, 전체나열")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/categories-not-hierarchy")
    public ResponseEntity<List<CategoryDto>> getCategoriesNotHierarchy() {
        return ResponseEntity.ok(categoryService.getAllCategoriesNotHierarchy());
    }
}
