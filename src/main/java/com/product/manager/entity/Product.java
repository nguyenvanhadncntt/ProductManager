package com.product.manager.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "description")
	private String description;
	
	@CreatedDate
	@Column(name = "created_date")
	private Instant createdDate = Instant.now();
	
	@LastModifiedDate
	@Column(name = "updated_date")
	private Instant updatedDate = Instant.now();
	
//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;
	
//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	private User updatedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	public Product() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public Instant getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Instant updatedDate) {
		this.updatedDate = updatedDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
