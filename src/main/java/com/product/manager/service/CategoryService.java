package com.product.manager.service;

import com.product.manager.dto.CategoryDTO;
import com.product.manager.exception.NotFoundException;

import java.util.List;

public interface CategoryService {

    public List<CategoryDTO> getAllCategories();

    public CategoryDTO getCategory(Long categoryId) throws NotFoundException;

    public CategoryDTO createCategory(CategoryDTO categoryDTO);

    public void updateCategory(Long id, CategoryDTO categoryDTO) throws NotFoundException;

    void deleteCategory(Long categoryId) throws NotFoundException;

}
