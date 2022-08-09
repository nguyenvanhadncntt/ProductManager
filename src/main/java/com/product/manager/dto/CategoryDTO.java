package com.product.manager.dto;

import com.product.manager.entity.Category;

import javax.validation.constraints.NotNull;

public class CategoryDTO {

    private Long id;

    @NotNull
    private String name;

    public CategoryDTO() {

    }

    public CategoryDTO(String name) {
        this.name = name;
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Category toModel(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getId(), categoryDTO.getName());
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


    public static CategoryDTO fromEntity(Category category) {
        return new CategoryDTO(category.getId(), category.getName());

    }

}
