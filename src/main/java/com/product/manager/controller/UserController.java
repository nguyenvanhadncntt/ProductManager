package com.product.manager.controller;

import java.security.Principal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.manager.constant.AuthoritiesConstants;
import com.product.manager.constant.ErrorMsgPattern;
import com.product.manager.dto.UserCreationDTO;
import com.product.manager.dto.UserDTO;
import com.product.manager.exception.BadRequestException;
import com.product.manager.service.UserService;
import com.product.manager.util.PageUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<List<UserDTO>> getUsers(Pageable page, String query) {
		LOGGER.info("Get Users with query: {}, pageNumber: {}, pageSize", query, page.getPageNumber(), page.getPageSize());
		Page<UserDTO> userDtoPage = userService.searchUser(query, page);
		HttpHeaders headers = PageUtil.createHeaderForPaganation(userDtoPage);
		return new ResponseEntity<List<UserDTO>>(userDtoPage.getContent(), headers, HttpStatus.OK);
	}
	
	@GetMapping("/current-user")
	public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
		LOGGER.info("Get Current User: {}", principal.getName());
		UserDTO user = userService.getUserByEmail(principal.getName());
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/{id}")
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		LOGGER.info("Get User with id: {}", id);
		UserDTO user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<UserCreationDTO> createUser(@RequestBody UserCreationDTO userCreation) {
		LOGGER.info("Create new user: {}", userCreation.getEmail());
		if (userService.checkEmailAlreadyExist(userCreation.getEmail())) {
			throw new BadRequestException(String.format(ErrorMsgPattern.EMAIL_ALREADY_EXIST, userCreation.getEmail()));
		}
		if (!StringUtils.equals(userCreation.getPassword(), userCreation.getPasswordConfirm())) {
			throw new BadRequestException(ErrorMsgPattern.PASSWORD_CONFIRM_NOT_MATCH);
		}
		return ResponseEntity.ok(userService.createNewUser(userCreation));
	}
	
	@PutMapping
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<UserCreationDTO> updateUser(@RequestBody UserCreationDTO userCreation) {
		LOGGER.info("Create new user: {}", userCreation.getEmail());
		if (!userService.checkUserAlreadyExistById(userCreation.getId())) {
			throw new BadRequestException(String.format(ErrorMsgPattern.USER_NOT_FOUND, userCreation.getId()));
		}

		return ResponseEntity.ok(userService.createNewUser(userCreation));
	}
}
