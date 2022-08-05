package com.product.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.manager.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
