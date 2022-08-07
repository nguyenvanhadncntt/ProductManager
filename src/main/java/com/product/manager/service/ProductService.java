package com.product.manager.service;

import org.springframework.web.multipart.MultipartFile;

import com.product.manager.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    void importProductFromCSVFile(MultipartFile file);
    public List<ProductDTO> getAllProducts();

    public ProductDTO getProduct(Long id);

    public ProductDTO createProduct(ProductDTO productDTO);

    public void updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long productId);
}
