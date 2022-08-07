package com.product.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.manager.dto.CategoryDTO;
import com.product.manager.exception.BadRequestException;
import com.product.manager.exception.NotFoundException;
import com.product.manager.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

class CategoryControllerTest extends ControllerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getAllCategories() throws Exception {
        String token = getUserToken();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        categoryDTOS.add(new CategoryDTO(1L, "AAA"));
        categoryDTOS.add(new CategoryDTO(2L, "BBB"));

        when(categoryService.getAllCategories()).thenReturn(categoryDTOS);
        MvcResult mvcResult = mvc.perform(get("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":1,\"name\":\"AAA\"},{\"id\":2,\"name\":\"BBB\"}]", content);
    }

    @Test
    void getCategory() throws Exception {
        String token = getUserToken();
        CategoryDTO categoryDTO = new CategoryDTO(1L, "AAA");

        when(categoryService.getCategory(1L)).thenReturn(categoryDTO);
        MvcResult mvcResult = mvc.perform(get("/api/categories/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"name\":\"AAA\"}", content);
    }

    @Test
    void createCategory() throws Exception {
        String token = getUserToken();
        CategoryDTO newCategory = new CategoryDTO("AAA");
        CategoryDTO saveCategory = new CategoryDTO(1L, "AAA");

        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(saveCategory);

        String url = "/api/categories";
        MvcResult mvcResult = mvc.perform(post(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory))
                        .with(csrf())
                ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"name\":\"AAA\"}", content);
    }

    @Test
    void createAlreadyExistCategory() throws Exception {
        String token = getUserToken();
        CategoryDTO newCategory = new CategoryDTO("AAA");

        doThrow(new BadRequestException("Category with name AAA is already exist")).when(categoryService).createCategory(any(CategoryDTO.class));

        String url = "/api/categories";
        MvcResult mvcResult = mvc.perform(post(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCategory))
                .with(csrf())
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Category with name AAA is already exist", content);
    }

    @Test
    void updateCategory() throws Exception {
        String token = getUserToken();
        CategoryDTO category = new CategoryDTO("AAA");

        String url = "/api/categories/1";
        MvcResult mvcResult = mvc.perform(put(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category))
                .with(csrf())
        ).andReturn();

        verify(categoryService).updateCategory(eq(1L), any(CategoryDTO.class));

        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    void updateCategoryNotExists() throws Exception {
        String token = getUserToken();
        CategoryDTO category = new CategoryDTO("AAA");

        doThrow(new NotFoundException("Category with id 1 does not exist")).when(categoryService).updateCategory(eq(1L), any(CategoryDTO.class));

        String url = "/api/categories/1";
        MvcResult mvcResult = mvc.perform(put(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category))
                .with(csrf())
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Category with id 1 does not exist", content);
    }

    @Test
    void deleteCategory() throws Exception {
        String token = getUserToken();
        String url = "/api/categories/1";

        MvcResult mvcResult = mvc.perform(delete(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(csrf())
        ).andReturn();

        verify(categoryService).deleteCategory(1L);
        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }

}