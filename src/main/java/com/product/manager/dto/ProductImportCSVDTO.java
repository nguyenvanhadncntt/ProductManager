package com.product.manager.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.opencsv.bean.CsvBindByName;

public class ProductImportCSVDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@CsvBindByName
	private String name;
	
	@CsvBindByName
	private BigDecimal price;
	
	@CsvBindByName
	private String description;
	
	@CsvBindByName
	private String category;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
