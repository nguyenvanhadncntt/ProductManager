package com.product.manager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.product.manager.dto.UserLoginDTO;

public class AuthenticationControllerTest extends ControllerBaseTest {
	
	@Test
	public void testAuthenticateWithSuccess() throws JsonProcessingException, Exception {
		UserLoginDTO userLogin = new UserLoginDTO("admin@localhost.com", "admin");
	    
	    MvcResult result = mvc.perform(MockMvcRequestBuilders.post(AUTHENTICATE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(userLogin)))
		.andExpect(status().isOk())
        .andReturn();
	    assertNotNull(result.getResponse());
	    
		userLogin = new UserLoginDTO("user@localhost.com", "user");
		result = mvc.perform(MockMvcRequestBuilders.post(AUTHENTICATE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(userLogin)))
		.andExpect(status().isOk())
        .andReturn();
		assertNotNull(result.getResponse());
	}
	
	@Test
	public void testAuthenticateWithWrongCredentials() throws JsonProcessingException, Exception {
		UserLoginDTO userLogin = new UserLoginDTO("wrongEmail@localhost.com", "wrongpassword");
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(AUTHENTICATE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(userLogin)))
		.andExpect(status().is(401)).andReturn();
		assertEquals("Unauthorized", result.getResponse().getErrorMessage());
	}
}
