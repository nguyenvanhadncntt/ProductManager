package com.product.manager.service;

import com.product.manager.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    public List<CategoryDTO> getAllCategories();

    public CategoryDTO getCategory(Long categoryId);

    public CategoryDTO createCategory(CategoryDTO categoryDTO);

    public void updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long categoryId);

}
