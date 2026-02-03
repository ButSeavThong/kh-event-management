package com.thong.event.feature.category;

import com.thong.event.domain.Category;
import com.thong.event.feature.category.dto.CategoryRequest;
import com.thong.event.feature.category.dto.CategoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category fromCreateCategoryRequest(CategoryRequest categoryRequest);
    List<CategoryResponse> toListOfCategoryResponse(List<Category> categoryList);
    CategoryResponse toCategoryResponse(Category category);
}
