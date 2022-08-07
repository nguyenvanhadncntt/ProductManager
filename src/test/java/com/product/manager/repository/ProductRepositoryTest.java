package com.product.manager.repository;

import com.product.manager.BaseTest;
import com.product.manager.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductRepositoryTest extends BaseTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void teardown() {
        productRepository.deleteAll();
    }

    @Test
    void findProductByName() {
        String name = "bicycle";
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(10));

        productRepository.save(product);

        List<Product> products = productRepository.findProductByName(name);

        assertEquals(1, products.size());
        assertEquals("bicycle", products.get(0).getName());
    }

}