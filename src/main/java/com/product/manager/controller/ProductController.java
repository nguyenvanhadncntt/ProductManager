package com.product.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.manager.constant.AuthoritiesConstants;
import com.product.manager.service.ProductService;

@RestController
@RequestMapping("/api/products")
@Secured(AuthoritiesConstants.USER)
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/import-csv")
	public ResponseEntity<Void> importProductFromCSVFile(@RequestParam("file") MultipartFile file) {
		productService.importProductFromCSVFile(file);
		return ResponseEntity.ok().build();
	}
}
