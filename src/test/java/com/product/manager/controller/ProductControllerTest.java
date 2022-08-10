package com.product.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.manager.dto.ProductDTO;
import com.product.manager.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductControllerTest extends ControllerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private final static String PRODUCT_URL = BASE_URL + "api/products";

    @Test
    public void importProductCSVTest() throws Exception {
        final byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/csv/products.csv"));
        String token = getUserToken();
        mvc.perform(multipart(PRODUCT_URL + "/import-csv").file("file", bytes)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

//    @Test
//    void getAllProducts() throws Exception {
//        String token = getUserToken();
//        List<ProductDTO> productDTOS = new ArrayList<>();
//
//        ProductDTO productDTO1 = new ProductDTO(1L, "A", BigDecimal.valueOf(33), "product A", null, null, null, null, null);
//        ProductDTO productDTO2 = new ProductDTO(1L, "B", BigDecimal.valueOf(11), "product B", null, null, null, null, null);
//        productDTOS.add(productDTO1);
//        productDTOS.add(productDTO2);
//
//        when(productService.getAllProducts(any(Pageable.class))).thenReturn(productDTOS);
//        MvcResult mockMvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/products")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
//                .andReturn();
//
//        int status = mockMvcResult.getResponse().getStatus();
//        assertEquals(200, status);
//        String content = mockMvcResult.getResponse().getContentAsString();
//        ProductDTO[] productDTOActual = objectMapper.readValue(content, ProductDTO[].class);
//        assertEquals(2, productDTOActual.length);
//        assertEquals("A", productDTOActual[0].getName());
//        assertEquals(BigDecimal.valueOf(33), productDTOActual[0].getPrice());
//        assertEquals("product A", productDTOActual[0].getDescription());
//        assertEquals("B", productDTOActual[1].getName());
//        assertEquals(BigDecimal.valueOf(11), productDTOActual[1].getPrice());
//        assertEquals("product B", productDTOActual[1].getDescription());
//    }

    @Test
    void getProduct() throws Exception {
        String token = getUserToken();
        ProductDTO productDTO = new ProductDTO(1L, "A", BigDecimal.valueOf(33), "product A", null, null, null, null, null);

        when(productService.getProduct(1L)).thenReturn(productDTO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/products/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        ProductDTO productDTOActual = objectMapper.readValue(content, ProductDTO.class);
        assertEquals("A", productDTOActual.getName());
        assertEquals(BigDecimal.valueOf(33), productDTOActual.getPrice());
        assertEquals("product A", productDTOActual.getDescription());
    }

    @Test
    void createProduct() throws Exception {
        String token = getUserToken();
        ProductDTO newProduct = new ProductDTO(1L, "A", BigDecimal.valueOf(33), "product A", null, null, null, null, 3L);
        ProductDTO saveProduct = new ProductDTO(1L, "A", BigDecimal.valueOf(33), "product A", null, null, null, null, 3L);

        when(productService.createProduct(any(ProductDTO.class), any(String.class))).thenReturn(saveProduct);

        String url = "/api/products";
        MvcResult mvcResult = mvc.perform(post(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct))
                .with(csrf())
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        ProductDTO productDTOResponse = objectMapper.readValue(content, ProductDTO.class);
        assertEquals("A", productDTOResponse.getName());
        assertEquals(BigDecimal.valueOf(33), productDTOResponse.getPrice());
        assertEquals("product A", productDTOResponse.getDescription());
        assertEquals(3L, productDTOResponse.getCategoryId());
    }

    @Test
    void updateProduct() throws Exception {
        String token = getUserToken();
        ProductDTO updateProduct = new ProductDTO(1L, "A", BigDecimal.valueOf(33), "product A", null, null, null, null, null);

        String url = "/api/products/1";
        MvcResult mvcResult = mvc.perform(put(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProduct))
                .with(csrf())
        ).andReturn();

        verify(productService).updateProduct(eq(1L), any(ProductDTO.class), any(String.class));

        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    void deleteProduct() throws Exception {
        String token = getUserToken();
        String url = "/api/products/1";

        MvcResult mvcResult = mvc.perform(delete(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(csrf())
        ).andReturn();

        verify(productService).deleteProduct(1L);
        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }
}