package com.product.manager.service.impl;

import com.product.manager.BaseTest;
import com.product.manager.dto.CategoryDTO;
import com.product.manager.entity.Category;
import com.product.manager.exception.NotFoundException;
import com.product.manager.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class CategoryServiceImplTest extends BaseTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void getAllCategories() {
        categoryService.getAllCategories();
        verify(categoryRepository).findAll();
    }

    @Test
    void getCategory() throws NotFoundException {
        Category category1 = new Category(1L, "AAA");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        CategoryDTO categoryDTO = categoryService.getCategory(1L);

        assertEquals("AAA", categoryDTO.getName());
    }

    @Test
    void createCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "AAA");

        CategoryDTO categoryDTOActual = categoryService.createCategory(categoryDTO);

        ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(argumentCaptor.capture());
        Category category = argumentCaptor.getValue();
        assertEquals("AAA", category.getName());
        assertEquals(categoryDTO.getName(), categoryDTOActual.getName());
    }

    @Test
    void updateCategory() throws NotFoundException {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("BBB");
        Category category1 = new Category(1L, "AAA");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        categoryService.updateCategory(1L, categoryDTO);

        ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).saveAndFlush(argumentCaptor.capture());
        Category category = argumentCaptor.getValue();
        assertEquals("BBB", category.getName());
    }

    @Test
    void updateCategoryNotExists() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("BBB");
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            categoryService.updateCategory(1L, categoryDTO);
            fail();

        } catch (NotFoundException e) {
            assertEquals("Category with id 1 does not exist", e.getMessage());
        }
    }

    @Test
    void deleteCategory() throws NotFoundException {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        categoryService.deleteCategory(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategoryNotExists() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        try {
            categoryService.deleteCategory(1L);
            fail();
        } catch (NotFoundException e) {
            assertEquals("Category with id 1 does not exist", e.getMessage());
        }
    }

}