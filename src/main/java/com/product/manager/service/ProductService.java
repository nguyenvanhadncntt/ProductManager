package com.product.manager.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
	void importProductFromCSVFile(MultipartFile file);
}
