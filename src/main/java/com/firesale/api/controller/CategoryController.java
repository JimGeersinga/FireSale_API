package com.firesale.api.controller;

import com.firesale.api.dto.ApiResponse;
import com.firesale.api.dto.category.CategoryDTO;
import com.firesale.api.mapper.CategoryMapper;
import com.firesale.api.model.Category;
import com.firesale.api.dto.category.UpsertCategoryDTO;
import com.firesale.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> available() {
        final Collection<Category> auctions = categoryService.getAvailableCategories();
        return new ResponseEntity<>(new ApiResponse<>(true, auctions.stream().map(categoryMapper::toDTO).collect(Collectors.toList())), HttpStatus.OK);
    }

    @GetMapping("archived")
    @PreAuthorize("isAuthenticated() and @guard.isAdmin()")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> archived() {
        final Collection<Category> auctions = categoryService.getArchivedCategories();
        return new ResponseEntity<>(new ApiResponse<>(true, auctions.stream().map(categoryMapper::toDTO).collect(Collectors.toList())), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and @guard.isAdmin()")
    public ResponseEntity<ApiResponse<CategoryDTO>> create(@Valid @RequestBody UpsertCategoryDTO upsertCategoryDTO) {
        final Category category = categoryService.create(categoryMapper.toModel(upsertCategoryDTO));
        return new ResponseEntity<>(new ApiResponse<>(true, categoryMapper.toDTO(category)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() and @guard.isAdmin()")
    public void update(@PathVariable("id") final long id, @Valid @RequestBody final UpsertCategoryDTO upsertCategoryDTO) {
        categoryService.updateCategory(id, categoryMapper.toModel(upsertCategoryDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() and @guard.isAdmin()")
    public void delete(@PathVariable("id") final long id) {
        categoryService.deleteCategory(id);
    }

}
