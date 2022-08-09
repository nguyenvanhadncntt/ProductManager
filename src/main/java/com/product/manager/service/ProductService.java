package com.product.manager.service;

import org.springframework.web.multipart.MultipartFile;

import com.product.manager.dto.ProductDTO;
import com.product.manager.exception.NotFoundException;

import java.util.List;

public interface ProductService {

    void importProductFromCSVFile(MultipartFile file);
    public List<ProductDTO> getAllProducts();

    public ProductDTO getProduct(Long id) throws NotFoundException;

    public ProductDTO createProduct(ProductDTO productDTO) throws NotFoundException;

    public void updateProduct(Long id, ProductDTO productDTO) throws NotFoundException;

    void deleteProduct(Long productId) throws NotFoundException;
}
