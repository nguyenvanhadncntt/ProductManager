package com.product.manager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.product.manager.constant.ErrorMsgPattern;
import com.product.manager.dto.UserCreationDTO;
import com.product.manager.dto.UserDTO;
import com.product.manager.entity.User;
import com.product.manager.repository.UserRepository;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends ControllerBaseTest {
	private final String USER_URL = BASE_URL + "api/users";

	@Autowired
	private UserRepository userRepository;

	@Test
	@Order(1)
	public void testGetAllUserNotSearchQuery() throws JsonProcessingException, Exception {
		MockHttpServletResponse response = getResponseForSearchUser("", "0");

		assertNotNull(response.getContentAsString());

		List<UserDTO> userDtos = objectMapper.readValue(response.getContentAsString(),
				new TypeReference<List<UserDTO>>() {
				});

		assertEquals(2, userDtos.size());
		assertEquals("admin@localhost.com", userDtos.get(0).getEmail());
		assertEquals("user@localhost.com", userDtos.get(1).getEmail());
	}

	@Test
	@Order(2)
	public void testGetAllUserSearchHaveQuery() throws JsonProcessingException, Exception {
		// search with key word admin
		MockHttpServletResponse response = getResponseForSearchUser("admin", "0");

		assertNotNull(response.getContentAsString());

		List<UserDTO> userDtos = objectMapper.readValue(response.getContentAsString(),
				new TypeReference<List<UserDTO>>() {
				});

		assertEquals(1, userDtos.size());
		assertEquals("admin@localhost.com", userDtos.get(0).getEmail());

		// search with key word user
		response = getResponseForSearchUser("user", "0");

		userDtos = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<UserDTO>>() {
		});

		assertEquals(1, userDtos.size());
		assertEquals("user@localhost.com", userDtos.get(0).getEmail());
	}

	@Test
	@Order(3)
	public void testGetCurrentUserInfo() throws JsonProcessingException, Exception {
		var adminToken = getAdminToken();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(USER_URL + "/current-user")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		MockHttpServletResponse response = result.getResponse();

		UserDTO user = objectMapper.readValue(response.getContentAsString(), UserDTO.class);

		assertEquals("Admin", user.getFirstName());
		assertEquals("admin@localhost.com", user.getEmail());
	}

	@Test
	@Order(4)
	public void testGetUserById() throws JsonProcessingException, Exception {
		var adminToken = getAdminToken();

		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(USER_URL + "/4").header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();

		UserDTO user = objectMapper.readValue(response.getContentAsString(), UserDTO.class);

		assertEquals("User", user.getFirstName());
		assertEquals("user@localhost.com", user.getEmail());
	}

	@Test
	@Order(5)
	public void testCreateNewUser() throws JsonProcessingException, Exception {
		var adminToken = getAdminToken();

		UserCreationDTO userCreationDTO = new UserCreationDTO();
		userCreationDTO.setEmail("user3@localhost.com");
		userCreationDTO.setFirstName("user3");
		userCreationDTO.setLastName("Third user");
		userCreationDTO.setPassword("user3");
		userCreationDTO.setPasswordConfirm("user3");
		userCreationDTO.setRoleId(2l);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post(USER_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(ow.writeValueAsString(userCreationDTO)))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();

		UserCreationDTO user = objectMapper.readValue(response.getContentAsString(), UserCreationDTO.class);

		assertEquals("user3", user.getFirstName());
		assertEquals("user3@localhost.com", user.getEmail());

		List<User> users = userRepository.findAll();

		assertEquals(3, users.size());
	}

	@Test
	@Order(6)
	public void testCreateUserAlreadyExistInSystem() throws JsonProcessingException, Exception {
		var adminToken = getAdminToken();

		UserCreationDTO userCreationDTO = new UserCreationDTO();
		userCreationDTO.setEmail("admin@localhost.com");
		userCreationDTO.setFirstName("user3");
		userCreationDTO.setLastName("Third user");
		userCreationDTO.setPassword("user3");
		userCreationDTO.setPasswordConfirm("user3");
		userCreationDTO.setRoleId(2l);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post(USER_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(ow.writeValueAsString(userCreationDTO)))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();
		assertEquals("The Email admin@localhost.com already exist in the system", response.getContentAsString());
	}

	@Test
	@Order(7)
	public void testCreateUserPasswordNotMatch() throws JsonProcessingException, Exception {
		var adminToken = getAdminToken();

		UserCreationDTO userCreationDTO = new UserCreationDTO();
		userCreationDTO.setEmail("user4@localhost.com");
		userCreationDTO.setFirstName("user4");
		userCreationDTO.setLastName("Four user");
		userCreationDTO.setPassword("user4");
		userCreationDTO.setPasswordConfirm("user3");
		userCreationDTO.setRoleId(2l);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post(USER_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(ow.writeValueAsString(userCreationDTO)))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();
		assertEquals(ErrorMsgPattern.PASSWORD_CONFIRM_NOT_MATCH, response.getContentAsString());
	}

	@Test
	@Order(8)
	public void testUpdateUser() throws JsonProcessingException, Exception {
		var adminToken = getAdminToken();

		UserCreationDTO userCreationDTO = new UserCreationDTO();
		userCreationDTO.setId(4l);
		userCreationDTO.setEmail("usern@localhost.com");
		userCreationDTO.setLastName("Four user");
		userCreationDTO.setRoleId(2l);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.put(USER_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(ow.writeValueAsString(userCreationDTO)))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();

		UserCreationDTO user = objectMapper.readValue(response.getContentAsString(), UserCreationDTO.class);

		assertEquals("Four user", user.getLastName());
		assertEquals("usern@localhost.com", user.getEmail());
	}
	
	@Test
	@Order(9)
	public void testUpdateUserNotFound() throws JsonProcessingException, Exception {
		var adminToken = getAdminToken();

		UserCreationDTO userCreationDTO = new UserCreationDTO();
		userCreationDTO.setEmail("usern@localhost.com");
		userCreationDTO.setLastName("Four user");
		userCreationDTO.setRoleId(2l);
		userCreationDTO.setId(10000l);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.put(USER_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(ow.writeValueAsString(userCreationDTO)))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(String.format(ErrorMsgPattern.USER_NOT_FOUND, 10000), response.getContentAsString());
	}

	private MockHttpServletResponse getResponseForSearchUser(String query, String page)
			throws JsonProcessingException, Exception {
		String token = getAdminToken();

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get(USER_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.param("query", query).param("page", page).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		return result.getResponse();
	}
}
