package com.product.manager.service.impl;

import com.product.manager.dto.CategoryDTO;
import com.product.manager.entity.Category;
import com.product.manager.exception.BadRequestException;
import com.product.manager.exception.NotFoundException;
import com.product.manager.repository.CategoryRepository;
import com.product.manager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());
        return categories;
    }

    @Override
    public CategoryDTO getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id %s does not found", categoryId)));

        return CategoryDTO.fromEntity(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        List<Category> categoryDTOS = categoryRepository.findCategoryByName(categoryDTO.getName());
        if (!categoryDTOS.isEmpty()) {
            throw new BadRequestException(String.format("Category with name %s is already exist", categoryDTO.getName()));
        }
        Category category = CategoryDTO.toModel(categoryDTO);
        categoryRepository.save(category);
        return CategoryDTO.fromEntity(category);
    }

    @Override
    public void updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id %s does not exist", categoryId)));

        category.setName(categoryDTO.getName());
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException(String.format("Category with id %s does not exist", categoryId));
        }
        categoryRepository.deleteById(categoryId);
    }
}
