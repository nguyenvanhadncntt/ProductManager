package com.product.manager.controller;

import com.product.manager.constant.AuthoritiesConstants;
import com.product.manager.dto.ProductDTO;
import com.product.manager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@Secured(AuthoritiesConstants.USER)
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/api/products/import-csv")
    public ResponseEntity<Void> importProductFromCSVFile(@RequestParam("file") MultipartFile file) {
        productService.importProductFromCSVFile(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId) {
        ProductDTO productDTO = productService.getProduct(productId);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO product, UriComponentsBuilder uriComponentsBuilder) {
        ProductDTO productDTO = productService.createProduct(product);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/products/{id}").buildAndExpand(productDTO.getId()).toUri())
                .body(productDTO);
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<HttpStatus> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductDTO product) {
        productService.updateProduct(productId, product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
