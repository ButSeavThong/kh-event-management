package com.thong.event.feature.admin.controller;

import com.thong.event.feature.category.CategoryRepository;
import com.thong.event.feature.category.CategoryService;
import com.thong.event.feature.category.dto.CategoryRequest;
import com.thong.event.feature.category.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor // ready
public class AdminCategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }
    
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
//    public ResponseEntity<Category> updateCategory(
//            @PathVariable Long id,
//            @Valid @RequestBody CategoryRequest request) {
//        Category category = categoryService.updateCategory(id, request);
//        return ResponseEntity.ok(category);
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}