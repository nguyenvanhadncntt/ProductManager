package com.product.manager.repository;

import com.product.manager.BaseTest;
import com.product.manager.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryRepositoryTest extends BaseTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void teardown() {
        categoryRepository.deleteAll();
    }

    @Test
    void findProductByName() {
        String name = "bicycle";
        Category category = new Category();
        category.setName(name);

        categoryRepository.save(category);

        List<Category> categories = categoryRepository.findCategoryByName(name);

        assertEquals(1, categories.size());
    }
}