package com.product.manager.controller;

import com.product.manager.constant.AuthoritiesConstants;
import com.product.manager.dto.CategoryDTO;
import com.product.manager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@Secured(AuthoritiesConstants.USER)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryDTOList);
    }

    @GetMapping("/api/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("categoryId") Long categoryId){
        CategoryDTO categoryDTO = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryDTO> getCategory(@Valid @RequestBody CategoryDTO categoryRequest, UriComponentsBuilder uriComponentsBuilder){
        CategoryDTO categoryDTO = categoryService.createCategory(categoryRequest);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/categories/{id}").buildAndExpand(categoryDTO.getId()).toUri())
                .body(categoryDTO);
    }

    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<HttpStatus> updateCategory(@PathVariable("categoryId") Long categoryId, @Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryId, categoryDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/categories/{categoryId}")
    public ResponseEntity<HttpStatus> updateCategory(@PathVariable("categoryId") Long categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
