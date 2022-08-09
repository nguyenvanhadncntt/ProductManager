package com.product.manager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.product.manager.dto.ProductDTO;
import com.product.manager.entity.Category;
import com.product.manager.entity.Product;
import com.product.manager.exception.NotFoundException;
import com.product.manager.repository.CategoryRepository;
import com.product.manager.repository.ProductRepository;

@ExtendWith({MockitoExtension.class})
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;


    @Test
    void getAllProducts() {
        productService.getAllProducts();
        verify(productRepository).findAll();
    }

    @Test
    void getProduct() throws NotFoundException {
        Category category = new Category(1L, "vehicle");

        Product product = new Product();
        product.setName("A");
        product.setDescription("product A");
        product.setPrice(BigDecimal.valueOf(10));
        product.setCategory(category);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO productDTO = productService.getProduct(1L);

        assertEquals("A", productDTO.getName());
        assertEquals(BigDecimal.valueOf(10), productDTO.getPrice());
        assertEquals("product A", productDTO.getDescription());
        assertEquals(1L, productDTO.getCategoryId());
    }

    @Test
    void createProduct() throws NotFoundException {
        ProductDTO createdProductDTO = new ProductDTO();
        createdProductDTO.setName("BBB");
        createdProductDTO.setPrice(BigDecimal.valueOf(10));
        createdProductDTO.setDescription("product BBB");
        createdProductDTO.setCategoryId(1L);

        when(categoryRepository.existsById(1L)).thenReturn(true);
        Category category = new Category(1L, "vehicle");
        when(categoryRepository.getOne(1L)).thenReturn(category);

        ProductDTO productDTO = productService.createProduct(createdProductDTO);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(argumentCaptor.capture());
        Product product = argumentCaptor.getValue();
        assertEquals("BBB", product.getName());
        assertEquals(BigDecimal.valueOf(10), product.getPrice());
        assertEquals("product BBB", product.getDescription());
        assertEquals(1L, product.getCategory().getId());
        assertEquals("vehicle", product.getCategory().getName());

        assertEquals(createdProductDTO.getName(), productDTO.getName());
        assertEquals(createdProductDTO.getDescription(), productDTO.getDescription());
        assertEquals(createdProductDTO.getPrice(), productDTO.getPrice());
        assertEquals(1L, productDTO.getCategoryId());
    }

    @Test
    void updateProduct() throws NotFoundException {
        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setName("BBB");
        updatedProductDTO.setPrice(BigDecimal.valueOf(10));

        Product product = new Product();
        product.setName("A");
        product.setDescription("product A");
        product.setPrice(BigDecimal.valueOf(10));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProduct(1L, updatedProductDTO);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).saveAndFlush(argumentCaptor.capture());
        Product productCaptor = argumentCaptor.getValue();
        assertEquals("BBB", productCaptor.getName());
        assertEquals(BigDecimal.valueOf(10), productCaptor.getPrice());
    }

    @Test
    void deleteProduct() throws NotFoundException {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteCategoryNotExists() {
        when(productRepository.existsById(1L)).thenReturn(false);

        try {
            productService.deleteProduct(1L);
            fail();
        } catch (NotFoundException e) {
            assertEquals("Product with id 1 does not exist", e.getMessage());
        }
    }
}