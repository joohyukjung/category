package com.musinsa.category.service;

import com.musinsa.category.dto.CategoryDto;
import com.musinsa.category.entity.Category;
import com.musinsa.category.exception.DuplicateException;
import com.musinsa.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    public List<CategoryDto> getAllCategoriesV2() {
        List<CategoryDto> categories = categoryRepository.findAll().stream()
                .map(Category -> CategoryDto.from2(Category))
                .collect(Collectors.toList());
        // NOTE parentId가 0L이면 최상위 category 로 정의됨
        List result = makeHierarchy2(0L, categories);

        return result;
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

    /**
     * 계층구조 생성 v2
     * 전체 categories를 순회하며, childNodes 구분
     * @param categories
     * @return
     */
    private List<CategoryDto> makeHierarchy2(Long id, List<CategoryDto> categories) {
        List<CategoryDto> results = new ArrayList();

        CategoryDto category;
        List<CategoryDto> parentNodes = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            category = categories.get(i);
            if (category.getParentId() == id) {
                parentNodes.add(category);
                results.add(category);
            } else {
                getChildNodes(parentNodes, category);
                parentNodes.add(category);
            }
        }
        return results;
    }

    private void getChildNodes(List<CategoryDto> parentNodes, CategoryDto node) {
        for (int i = parentNodes.size() - 1; i >= 0; i--) {
            CategoryDto pnode = parentNodes.get(i);
            if (pnode.getId() == node.getParentId()) {
                pnode.getChildNodes().add(node);
                return;
            }
        }
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
        // TODO Category를 DB에 계층구조 Path를 적용하여 삭제대상을 Like로 조회 후, 하위 Category를 한번에 지우는게 더 좋을 것 같다는 생각.각
        // TODO 대신 업데이트 할 경우, 자신을 바라보는 Path를 수정하는 것이 까다로울 수 있을 것 같다.
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
    public void deleteChildNodes(List<Category> childNodes) {
        for (int i = 0; i < childNodes.size() ; i++) {
            categoryRepository.deleteById(childNodes.get(i).getId());
            List<Category> childNods = categoryRepository.findByParentId(childNodes.get(i).getId());
            deleteChildNodes(childNods);
        }
    }

    public List<CategoryDto> getAllCategoriesNotHierarchy() {
        return categoryRepository.findAll().stream()
                .map(Category -> CategoryDto.from(Category))
                .collect(Collectors.toList());
    }
}
