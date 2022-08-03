package com.product.manager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.product.manager.dto.JWTToken;
import com.product.manager.dto.UserLoginDTO;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerBaseTest {
	protected final static String BASE_URL = "http://localhost:8080/";
	
	protected final static String AUTHENTICATE_URL = BASE_URL + "authenticate";
	
    @Autowired
    protected MockMvc mvc;
    
    ObjectMapper objectMapper = new ObjectMapper();
	
    ObjectWriter ow;
    
	@BeforeEach
	public void init() {
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ow = objectMapper.writer().withDefaultPrettyPrinter();
	}
	
	protected String getToken(String email, String password) throws JsonProcessingException, Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(AUTHENTICATE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(new UserLoginDTO(email, password)))).andReturn();
		MockHttpServletResponse response = result.getResponse();
		if (response.getContentAsString() != null) {
			JWTToken token = objectMapper.readValue(result.getResponse().getContentAsString(), JWTToken.class);			
			return token.getIdToken();
		}
		throw new AuthenticationCredentialsNotFoundException(response.getErrorMessage());
	}
}
