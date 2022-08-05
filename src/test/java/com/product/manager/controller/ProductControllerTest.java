package com.product.manager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

public class ProductControllerTest extends ControllerBaseTest {
	
	private final static String PRODUCT_URL = BASE_URL + "api/products";
	
	@Test
	public void importProductCSVTest() throws Exception {
		final byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/csv/products.csv"));
		String token = getUserToken();
		mvc.perform(multipart(PRODUCT_URL + "/import-csv").file("file", bytes)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
	}
}
