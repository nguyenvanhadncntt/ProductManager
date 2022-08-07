package com.product.manager.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.product.manager.convert.impl.ProductCSVConvertToEntity;
import com.product.manager.dto.ProductImportCSVDTO;
import com.product.manager.entity.Product;
import com.product.manager.repository.ProductRepository;
import com.product.manager.service.ProductService;
import com.product.manager.util.CSVUtil;

@Service
public class ProductServiceImpl implements ProductService {

	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductCSVConvertToEntity productCSVConvertToEntity;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	@Transactional
	public void importProductFromCSVFile(MultipartFile file) {
		try {
			List<ProductImportCSVDTO> productFromCSVs = CSVUtil.convertCsvFileToProductObject(file.getInputStream());
			List<Product> products = productCSVConvertToEntity.convertToProducts(productFromCSVs);
			productRepository.saveAll(products);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Cannot read csv file {}", file.getOriginalFilename());
		}
	}

}
