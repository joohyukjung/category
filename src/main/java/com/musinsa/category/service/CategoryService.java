package com.musinsa.category.service;

import com.musinsa.category.dto.CategoryDto;
import com.musinsa.category.entity.Category;
import com.musinsa.category.exception.DuplicateException;
import com.musinsa.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new DuplicateException("Category id["+id+"] not found.");
        }

        CategoryDto categoryDto = CategoryDto.from(category);
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

    /**
     * 재귀 - 계층구조 생성
     * @param categories
     * @return
     */
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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

    /**
     * 자신을 포함한 하위 자식 categories까지 삭제
     * @param id
     */
    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).orElse(null) == null) {
            throw new DuplicateException("Category id["+id+"] not found.");
        }

        categoryRepository.deleteById(id);

        List<Category> childNodes = categoryRepository.findByParentId(id);
        deleteChildNodes(childNodes);
    }

    /**
     * 재귀 - 하위 categories를 순회하며 자식노드까지 모두 삭제한다.
     * @param childNodes
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteChildNodes(List<Category> childNodes) {
        for (int i = 0; i < childNodes.size() ; i++) {
            categoryRepository.deleteById(childNodes.get(i).getId());
            List<Category> childNods = categoryRepository.findByParentId(childNodes.get(i).getId());
            System.out.println(childNods);
            deleteChildNodes(childNods);
        }
    }

    public List<CategoryDto> getAllCategoriesNotHierarchy() {
        return categoryRepository.findAll().stream()
                .map(Category -> CategoryDto.from(Category))
                .collect(Collectors.toList());
    }
}
