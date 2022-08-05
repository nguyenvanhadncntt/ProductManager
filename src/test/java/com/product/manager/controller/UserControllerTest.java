package com.product.manager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.product.manager.dto.UserDTO;

public class UserControllerTest extends ControllerBaseTest {
	private final String USER_URL = BASE_URL + "api/users";
	
	@Test
	public void testGetAllUserNotSearchQuery() throws JsonProcessingException, Exception {
		MockHttpServletResponse response = getResponseForSearchUser("", "0");
		
		assertNotNull(response.getContentAsString());
		
		List<UserDTO> userDtos = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<UserDTO>>(){});
		
		assertEquals(2, userDtos.size());
		assertEquals("admin@localhost.com", userDtos.get(0).getEmail());
		assertEquals("user@localhost.com", userDtos.get(1).getEmail());
	}
	
	@Test
	public void testGetAllUserSearchHaveQuery() throws JsonProcessingException, Exception {
		// search with key word admin
		MockHttpServletResponse response = getResponseForSearchUser("admin", "0");
		
		assertNotNull(response.getContentAsString());
		
		List<UserDTO> userDtos = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<UserDTO>>(){});
		
		assertEquals(1, userDtos.size());
		assertEquals("admin@localhost.com", userDtos.get(0).getEmail());
		
		// search with key word user
		response = getResponseForSearchUser("user", "0");
		
		userDtos = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<UserDTO>>(){});
		
		assertEquals(1, userDtos.size());
		assertEquals("user@localhost.com", userDtos.get(0).getEmail());
	}
	
	private MockHttpServletResponse getResponseForSearchUser(String query, String page) throws JsonProcessingException, Exception {
		String token = getAdminToken();
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders
				.get(USER_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.param("query", query)
				.param("page", page)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		return result.getResponse();
	}
}
