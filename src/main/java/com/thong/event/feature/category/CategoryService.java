package com.thong.event.feature.category;

import com.thong.event.domain.Category;
import com.thong.event.exception.ResourceNotFoundException;
import com.thong.event.feature.category.dto.CategoryRequest;
import com.thong.event.feature.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;


    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return  categoryMapper.toListOfCategoryResponse( categories );
    }
    
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Category with id %s not found", id)));
        return categoryMapper.toCategoryResponse(category);
    }
    
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
            .name(request.getName())
            .description(request.getDescription())
            .build();
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

}