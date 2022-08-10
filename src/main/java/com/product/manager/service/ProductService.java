package com.product.manager.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.product.manager.dto.ProductDTO;
import com.product.manager.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ProductService {

    void importProductFromCSVFile(MultipartFile file);

    Page<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO getProduct(Long id) throws NotFoundException;

    ProductDTO createProduct(ProductDTO productDTO, String emailUser) throws NotFoundException;

    void updateProduct(Long id, ProductDTO productDTO, String emailUser) throws NotFoundException;

    void deleteProduct(Long productId) throws NotFoundException;

    void exportProductToCSV(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;

}