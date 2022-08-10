package com.product.manager.controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.product.manager.constant.AuthoritiesConstants;
import com.product.manager.dto.ProductDTO;
import com.product.manager.exception.NotFoundException;
import com.product.manager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@Secured(AuthoritiesConstants.USER)
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/import-csv")
    public ResponseEntity<Void> importProductFromCSVFile(@RequestParam("file") MultipartFile file) {
        productService.importProductFromCSVFile(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId) throws NotFoundException {
        ProductDTO productDTO = productService.getProduct(productId);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping()
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO product, UriComponentsBuilder uriComponentsBuilder) throws NotFoundException {
        ProductDTO productDTO = productService.createProduct(product);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/products/{id}").buildAndExpand(productDTO.getId()).toUri())
                .body(productDTO);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<HttpStatus> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductDTO product) throws NotFoundException {
        productService.updateProduct(productId, product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("productId") Long productId) throws NotFoundException {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export-csv")
    public void exportProductToCSV(HttpServletResponse response) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        Date now = new Date();
        var filename = "products_" + now + ".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        productService.exportProductToCSV(response);
    }

}
