package com.musinsa.category.service;

import com.musinsa.category.dto.CategoryDto;
import com.musinsa.category.entity.Category;
import com.musinsa.category.exception.DuplicateException;
import com.musinsa.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto getCategory(Long id) {
        CategoryDto categoryDto = CategoryDto.from(categoryRepository.findById(id).orElse(null));
        categoryDto.setChildNodes(getCategories(id));
        return categoryDto;
    }

    public List<CategoryDto> getCategories(Long id) {
        List<CategoryDto> categories = categoryRepository.findByParentId(id).stream()
                .map(Category -> CategoryDto.from(Category))
                .collect(Collectors.toList());
        List result = makeHierarchy(categories);

        return result;
    }

    public List<CategoryDto> getAllCategories() {
        return getCategories(0L);
    }

    private List makeHierarchy(List categories) {
        List array = new ArrayList();
        for (int i = 0; i < categories.size(); i++) {
            CategoryDto categoryDto = (CategoryDto) categories.get(i);
            List childNodes = categoryRepository.findByParentId(categoryDto.getId()).stream()
                    .map(Category -> CategoryDto.from(Category))
                    .collect(Collectors.toList());
            List categoryList =this.makeHierarchy(childNodes);
            categoryDto.setChildNodes(categoryList);
            array.add(categoryDto);
        }
        return array;
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .value(categoryDto.getValue())
                .parentId(categoryDto.getParentId())
                .build();

        return CategoryDto.from(categoryRepository.save(category));
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new DuplicateException("Category id["+id+"] not found.");
        }

        if (category.getParentId() > 0
                && categoryRepository.findById(category.getParentId()).orElse(null) == null) {
            throw new DuplicateException("Category id["+category.getParentId()+"] not found.");
        }

        Category cate = Category.builder()
                .id(id)
                .value(categoryDto.getValue())
                .parentId(categoryDto.getParentId())
                .build();

        return CategoryDto.from(categoryRepository.save(cate));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).orElse(null) == null) {
            throw new DuplicateException("Category id["+id+"] not found.");
        }

        categoryRepository.deleteById(id);
    }
}
