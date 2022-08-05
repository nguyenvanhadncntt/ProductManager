package com.product.manager.controller;

import com.product.manager.dto.UserDTO;
import com.product.manager.service.UserService;
import com.product.manager.util.PageUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<List<UserDTO>> getUsers(Pageable page, String query) {
		LOGGER.info("Get Users with query: {}, pageNumber: {}", query, page.getPageNumber());
		Page<UserDTO> userDtoPage = userService.searchUser(query, page);
		HttpHeaders headers = PageUtil.createHeaderForPaganation(userDtoPage);
		return new ResponseEntity<List<UserDTO>>(userDtoPage.getContent(), headers, HttpStatus.OK);
	}
}
