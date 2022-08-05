package com.product.manager.convert.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.product.manager.dto.ProductImportCSVDTO;
import com.product.manager.entity.Category;
import com.product.manager.entity.Product;
import com.product.manager.repository.CategoryRepository;

@Component
public class ProductCSVConvertToEntity {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private List<Category> loadAllCategories() {
		return categoryRepository.findAll();
	}
	
	private Category findCategoryByName(List<Category> categories, String categoryName) {
		return categories.stream().filter(
					category -> 
						StringUtils.lowerCase(category.getName())
							.contains(StringUtils.lowerCase(categoryName)))
			  .findFirst().orElse(null);
	}
	
	public Product convertToProduct(List<Category> categories, ProductImportCSVDTO csvdto) {
		Product product = new Product();
		product.setName(csvdto.getName());
		product.setPrice(csvdto.getPrice());
		product.setDescription(csvdto.getDescription());
		product.setCategory(findCategoryByName(categories, csvdto.getCategory()));
		
		return product;
	}
	
	public List<Product> convertToProducts(List<ProductImportCSVDTO> csvdtos) {
		
		if (CollectionUtils.isNotEmpty(csvdtos)) {
			List<Category> categories = loadAllCategories();
			return csvdtos.stream().map(csvDto -> convertToProduct(categories, csvDto)).collect(Collectors.toList());
		}
		
		return Collections.emptyList();
	}
}
